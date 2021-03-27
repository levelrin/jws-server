/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

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
final class OnRequestLinesTest {

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
        new OnRequestLines(
            socket,
            ioException -> {
                throw new IllegalStateException(
                    "Exception should not occur in this test.",
                    ioException
                );
            },
            lines -> {
                actual.addAll(lines);
                return Mockito.mock(WsServer.class);
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
        new OnRequestLines(
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

}
