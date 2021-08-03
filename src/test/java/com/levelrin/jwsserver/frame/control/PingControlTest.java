/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.End;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Tests.
 */
final class PingControlTest {

    @Test
    public void shouldMatchOpcodeForPingFrame() {
        MatcherAssert.assertThat(
            new PingControl(
                Mockito.mock(Socket.class)
            ).match("1001"),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    @SuppressWarnings("PMD.CloseResource")
    public void sectionShouldBeEnd() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final OutputStream output = Mockito.mock(OutputStream.class);
        Mockito.doReturn(output).when(socket).getOutputStream();
        MatcherAssert.assertThat(
            new PingControl(socket).section(),
            CoreMatchers.instanceOf(End.class)
        );
    }

    @Test
    @SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
    public void shouldSendPongFrame() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final OutputStream output = Mockito.mock(OutputStream.class);
        Mockito.doReturn(output).when(socket).getOutputStream();
        final ByteArrayOutputStream actual = new ByteArrayOutputStream();
        Mockito.doAnswer(invocation -> {
            actual.write(invocation.getArgument(0));
            return actual;
        }).when(output).write(Mockito.any());
        new PingControl(socket).section();
        MatcherAssert.assertThat(
            actual.toByteArray(),
            CoreMatchers.equalTo(
                new byte[] {
                    // -118 is equivalent to 10001010,
                    // which means it's a FIN pong frame.
                    -118,
                    // 0 is equivalent to 00000000,
                    // which means the payload data is not masked and length is 0.
                    0,
                }
            )
        );
    }

}
