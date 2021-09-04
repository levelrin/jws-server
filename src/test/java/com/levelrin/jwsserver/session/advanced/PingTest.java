/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.advanced;

import com.levelrin.jwsserver.io.CheckableOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests.
 */
final class PingTest {

    @Test
    @SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
    public void shouldSendFinPingFrameWithoutData() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final CheckableOutputStream stream = new CheckableOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        new Ping(socket).ping();
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                new byte[] {
                    // -119 is equivalent to 10001001,
                    // which means it's a FIN ping frame.
                    -119,
                    // The data is unmasked and its length is zero.
                    0,
                }
            )
        );
    }

    @Test
    @SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
    public void shouldSendFinPingFrameWithData() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final CheckableOutputStream stream = new CheckableOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        new Ping(socket).ping(new byte[] {1});
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                new byte[] {
                    // -119 is equivalent to 10001001,
                    // which means it's a FIN ping frame.
                    -119,
                    // The data is unmasked and its length is 1.
                    1,
                    // The data is 1.
                    1,
                }
            )
        );
    }

}
