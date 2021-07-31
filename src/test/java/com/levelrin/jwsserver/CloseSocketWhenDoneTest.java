/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.Socket;

/**
 * Tests.
 */
@SuppressWarnings("PMD.CloseResource")
final class CloseSocketWhenDoneTest {

    @Test
    public void shouldCloseSocketAfterOrigin() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final WsServer origin = Mockito.mock(WsServer.class);
        new CloseSocketWhenDone(
            socket,
            origin
        ).start();
        Mockito.verify(origin).start();
        Mockito.verify(socket).close();
    }

    @Test
    public void shouldCatchGenericExceptionAndCloseSocket() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final WsServer origin = Mockito.mock(WsServer.class);
        Mockito.doThrow(
            new IllegalStateException("Exception thrown by origin.")
        ).when(origin).start();
        try {
            new CloseSocketWhenDone(
                socket,
                origin
            ).start();
        } catch (final IllegalStateException ignored) {
            // Exception is expected.
        }
        Mockito.verify(socket).close();
    }

}
