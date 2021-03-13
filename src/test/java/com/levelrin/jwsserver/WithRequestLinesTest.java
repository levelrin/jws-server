/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.opening.OpeningResult;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests.
 */
@SuppressWarnings("PMD.CloseResource")
final class WithRequestLinesTest {

    @Test
    public void shouldReadRequestLines() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final InputStream inputStream = new FakeInputStream(
            """
            GET /path/to/websocket/endpoint HTTP/1.1
            Host: localhost
            Upgrade: websocket
            Connection: Upgrade
            Sec-WebSocket-Key: xqBt3ImNzJbYqRINxEFlkg==
            Origin: http://localhost
            Sec-WebSocket-Version: 13
            """
        );
        Mockito.when(socket.getInputStream()).thenReturn(inputStream);
        Mockito.when(socket.getOutputStream()).thenReturn(Mockito.mock(OutputStream.class));
        final List<String> actual = new ArrayList<>();
        new WithRequestLines(
            socket,
            ioException -> {
                throw new IllegalStateException(
                    "Exception should not occur in this test.",
                    ioException
                );
            },
            lines -> {
                actual.addAll(lines);
                return () -> new OpeningResult() {
                    @Override
                    public boolean success() {
                        return true;
                    }

                    @Override
                    public String message() {
                        return "Success!";
                    }
                };
            }
        ).start();
        MatcherAssert.assertThat(
            actual,
            Matchers.containsInRelativeOrder(
                "GET /path/to/websocket/endpoint HTTP/1.1",
                "Host: localhost",
                "Upgrade: websocket",
                "Connection: Upgrade",
                "Sec-WebSocket-Key: xqBt3ImNzJbYqRINxEFlkg==",
                "Origin: http://localhost",
                "Sec-WebSocket-Version: 13"
            )
        );
    }

    @Test
    public void shouldCatchIoException() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        Mockito.when(socket.getInputStream()).thenThrow(IOException.class);
        final AtomicBoolean caught = new AtomicBoolean(false);
        new WithRequestLines(
            socket,
            ioException -> caught.set(true),
            lines -> {
                throw new IllegalStateException(
                    "IOException should've occurred before reaching here."
                );
            }
        ).start();
        MatcherAssert.assertThat(
            caught.get(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldCloseSocketIfOpeningFailed() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final InputStream inputStream = new FakeInputStream(
            "Message doesn't matter for this test."
        );
        Mockito.when(socket.getInputStream()).thenReturn(inputStream);
        final OutputStream outputStream = Mockito.mock(OutputStream.class);
        Mockito.when(socket.getOutputStream()).thenReturn(outputStream);
        new WithRequestLines(
            socket,
            ioException -> {
                throw new IllegalStateException(
                    "Exception should not occur in this test.",
                    ioException
                );
            },
            lines -> () -> new OpeningResult() {
                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "Failed";
                }
            }
        ).start();
        Mockito.verify(socket).close();
    }

    @Test
    public void shouldWriteOpeningHandshakeResult() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final InputStream inputStream = new FakeInputStream(
            "Message doesn't matter for this test."
        );
        Mockito.when(socket.getInputStream()).thenReturn(inputStream);
        final FakeOutputStream outputStream = new FakeOutputStream();
        Mockito.when(socket.getOutputStream()).thenReturn(outputStream);
        new WithRequestLines(
            socket,
            ioException -> {
                throw new IllegalStateException(
                    "Exception should not occur in this test.",
                    ioException
                );
            },
            lines -> () -> new OpeningResult() {
                @Override
                public boolean success() {
                    return true;
                }

                @Override
                public String message() {
                    return "Yoi Yoi";
                }
            }
        ).start();
        MatcherAssert.assertThat(
            outputStream.message(),
            CoreMatchers.equalTo("Yoi Yoi")
        );
    }

    /**
     * We can inject a fake message for testing.
     */
    private static final class FakeInputStream extends InputStream {

        /**
         * The fake message that we are going to use.
         */
        private final String message;

        /**
         * It is used to return the correct byte of the fake message.
         */
        private int index;

        /**
         * Constructor.
         * @param message See {@link FakeInputStream#message}.
         */
        FakeInputStream(final String message) {
            super();
            this.message = message;
        }

        @Override
        public int read() {
            final byte[] bytes = this.message.getBytes(StandardCharsets.UTF_8);
            final byte value;
            if (this.index < bytes.length) {
                value = bytes[this.index];
                this.index = this.index + 1;
            } else {
                value = -1;
            }
            return value;
        }

    }

    /**
     * We can use this to check what has been written by the test object.
     */
    private static final class FakeOutputStream extends OutputStream {

        /**
         * It stores the bytes that has been written, but not flushed yet.
         */
        private final List<Byte> buffer = new ArrayList<>();

        /**
         * It stored the bytes that has been flushed.
         */
        private final List<Byte> flushed = new ArrayList<>();

        @Override
        public void write(final int part) {
            this.buffer.add((byte) part);
        }

        @Override
        public void flush() {
            this.flushed.addAll(this.buffer);
        }

        /**
         * It converts the message in bytes into String so we can check what has been written.
         * @return The message that has been written by the test object.
         */
        public String message() {
            final byte[] bytes = new byte[this.flushed.size()];
            for (int index = 0; index < this.flushed.size(); index = index + 1) {
                bytes[index] = this.flushed.get(index);
            }
            return new String(bytes, StandardCharsets.UTF_8);
        }

    }

}
