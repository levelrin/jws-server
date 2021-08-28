/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.component;

import com.levelrin.jwsserver.io.CheckableOutputStream;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Tests.
 */
@SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
final class OutgoingBodyTest {

    @Test
    public void shouldSendMessageWithLengthLessThan126() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final CheckableOutputStream stream = new CheckableOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final byte[] message = {
            0, 1, 2,
        };
        new OutgoingBody(socket, -126, message).send();
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        // Initial byte
        expected.write(-126);
        // Length of the message
        expected.write(3);
        // Payload data
        expected.write(message);
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                expected.toByteArray()
            )
        );
    }

    @Test
    public void shouldSendMessageWithLengthGreaterThan126AndLessThanOrEquals65535() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final CheckableOutputStream stream = new CheckableOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final ByteArrayOutputStream messageBuilder = new ByteArrayOutputStream();
        for (int iteration = 0; iteration < 65_535; iteration = iteration + 1) {
            messageBuilder.write(1);
        }
        final byte[] message = messageBuilder.toByteArray();
        new OutgoingBody(socket, -126, message).send();
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // Initial byte
        frame.write(-126);
        // 126 indicates that the next 2 bytes will be the payload length.
        frame.write(126);
        // 1111...1 (there are 16 ones) equals to 65,535, which is the payload length.
        // 11111111 (there are 8 ones) equals to -1 in decimal.
        // We need to send -1 two times because the payload length is represented by 2 bytes.
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
    public void shouldSendMessageWithLengthGreaterThan65535() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final CheckableOutputStream stream = new CheckableOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final ByteArrayOutputStream messageBuilder = new ByteArrayOutputStream();
        for (int iteration = 0; iteration < 65_536; iteration = iteration + 1) {
            messageBuilder.write(1);
        }
        final byte[] message = messageBuilder.toByteArray();
        new OutgoingBody(socket, -126, message).send();
        final ByteArrayOutputStream frame = new ByteArrayOutputStream();
        // Initial byte
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

}
