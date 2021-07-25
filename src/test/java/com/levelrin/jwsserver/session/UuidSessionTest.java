/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests.
 */
@SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
final class UuidSessionTest {

    @Test
    public void idShouldBeUnique() {
        final Socket socket = Mockito.mock(Socket.class);
        final UuidSession first = new UuidSession(socket);
        final UuidSession second = new UuidSession(socket);
        MatcherAssert.assertThat(
            first.id(),
            CoreMatchers.not(
                CoreMatchers.equalTo(
                    second.id()
                )
            )
        );
    }

    @Test
    public void idShouldBeCached() {
        final UuidSession session = new UuidSession(
            Mockito.mock(Socket.class)
        );
        MatcherAssert.assertThat(
            session.id(),
            CoreMatchers.equalTo(
                session.id()
            )
        );
    }

    @Test
    public void shouldSendTextMessageWithLengthLessThan126() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final FakeOutputStream stream = new FakeOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        new UuidSession(socket).sendMessage("Yoi");
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // -127 equals to 10000001, which means a FIN text frame.
        frame.write(-127);
        // Length of the message
        frame.write(3);
        // Payload data.
        frame.write("Yoi".getBytes(StandardCharsets.UTF_8));
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                frame.toByteArray()
            )
        );
    }

    @Test
    public void shouldSendTextMessageWithLengthGreaterThan126AndLessThanOrEquals65535() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final FakeOutputStream stream = new FakeOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final String message = "a".repeat(65_535);
        new UuidSession(socket).sendMessage(message);
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // -127 equals to 10000001, which means a FIN text frame.
        frame.write(-127);
        // 126 indicates that the next 2 bytes will be the payload length.
        frame.write(126);
        // 1111...1 (there are 16 ones) equals to 65,535, which is the payload length.
        // 11111111 (there are 8 ones) equals to -1 in decimal.
        // We gotta send -1 two times because the payload length is represented by 2 bytes.
        frame.write(-1);
        frame.write(-1);
        // Payload data.
        frame.write(message.getBytes(StandardCharsets.UTF_8));
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                frame.toByteArray()
            )
        );
    }

    @Test
    public void shouldSendTextMessageWithLengthGreaterThan65535() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final FakeOutputStream stream = new FakeOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final String message = "a".repeat(65_536);
        new UuidSession(socket).sendMessage(message);
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // -127 equals to 10000001, which means a FIN text frame.
        frame.write(-127);
        // 127 indicates that the next 8 bytes will be the payload length.
        frame.write(127);
        // 00000000 00000000 00000000 00000000 00000000 00000001 00000000 00000000
        // equals to 65,536, which is the payload length.
        // We gotta send 8 bytes that represents the above binary.
        frame.write(0);
        frame.write(0);
        frame.write(0);
        frame.write(0);
        frame.write(0);
        frame.write(1);
        frame.write(0);
        frame.write(0);
        // Payload data.
        frame.write(message.getBytes(StandardCharsets.UTF_8));
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                frame.toByteArray()
            )
        );
    }

    @Test
    public void shouldSendBinaryMessageWithLengthLessThan126() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final FakeOutputStream stream = new FakeOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final byte[] message = {
            0, 1, 2,
        };
        new UuidSession(socket).sendMessage(message);
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // -126 equals to 10000010, which means a FIN binary frame.
        frame.write(-126);
        // Length of the message
        frame.write(3);
        // Payload data.
        frame.write(message);
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                frame.toByteArray()
            )
        );
    }

    @Test
    public void shouldSendBinaryMessageWithLengthGreaterThan126AndLessThanOrEquals65535() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final FakeOutputStream stream = new FakeOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final ByteArrayOutputStream messageBuilder = new ByteArrayOutputStream();
        for (int iteration = 0; iteration < 65_535; iteration = iteration + 1) {
            messageBuilder.write(1);
        }
        final byte[] message = messageBuilder.toByteArray();
        new UuidSession(socket).sendMessage(message);
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // -126 equals to 10000010, which means a FIN binary frame.
        frame.write(-126);
        // 126 indicates that the next 2 bytes will be the payload length.
        frame.write(126);
        // 1111...1 (there are 16 ones) equals to 65,535, which is the payload length.
        // 11111111 (there are 8 ones) equals to -1 in decimal.
        // We gotta send -1 two times because the payload length is represented by 2 bytes.
        frame.write(-1);
        frame.write(-1);
        // Payload data.
        frame.write(message);
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                frame.toByteArray()
            )
        );
    }

    @Test
    public void shouldSendBinaryMessageWithLengthGreaterThan65535() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final FakeOutputStream stream = new FakeOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final ByteArrayOutputStream messageBuilder = new ByteArrayOutputStream();
        for (int iteration = 0; iteration < 65_536; iteration = iteration + 1) {
            messageBuilder.write(1);
        }
        final byte[] message = messageBuilder.toByteArray();
        new UuidSession(socket).sendMessage(message);
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // -126 equals to 10000010, which means a FIN binary frame.
        frame.write(-126);
        // 127 indicates that the next 8 bytes will be the payload length.
        frame.write(127);
        // 00000000 00000000 00000000 00000000 00000000 00000001 00000000 00000000
        // equals to 65,536, which is the payload length.
        // We gotta send 8 bytes that represents the above binary.
        for (int iteration = 0; iteration < 5; iteration = iteration + 1) {
            frame.write(0);
        }
        frame.write(1);
        frame.write(0);
        frame.write(0);
        // Payload data.
        frame.write(message);
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                frame.toByteArray()
            )
        );
    }

    /**
     * We can use this to check what data was sent by the session object.
     */
    private static final class FakeOutputStream extends OutputStream {

        /**
         * We will store the data sent by the session into this.
         */
        private final List<byte[]> dataCache = new ArrayList<>(1);

        @Override
        public void write(final int data) {
            throw new UnsupportedOperationException("Unexpected call.");
        }

        @Override
        public void write(final byte @NotNull [] data) {
            this.dataCache.add(data);
        }

        /**
         * We can use this to check the data sent by the session object.
         * @return Data sent by the session object.
         */
        public byte[] sentData() {
            return this.dataCache.get(0);
        }

    }

}
