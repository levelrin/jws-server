/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests.
 */
@SuppressWarnings("PMD.CloseResource")
final class OnConnectionTest {

    @Test
    public void shouldRunFunctionOnConnection() throws IOException {
        final ServerSocket serverSocket = Mockito.mock(ServerSocket.class);
        Mockito.when(serverSocket.accept()).thenReturn(Mockito.mock(Socket.class));
        final AtomicBoolean executed = new AtomicBoolean(false);
        new OnConnection(
            serverSocket,
            ioException -> {
                throw new IllegalStateException(
                    "Exception should not occur in this test.",
                    ioException
                );
            },
            socket -> () -> executed.set(true)
        ).start();
        MatcherAssert.assertThat(
            executed.get(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldCatchIoException() throws IOException {
        final ServerSocket serverSocket = Mockito.mock(ServerSocket.class);
        Mockito.when(serverSocket.accept()).thenThrow(IOException.class);
        final AtomicBoolean caught = new AtomicBoolean(false);
        new OnConnection(
            serverSocket,
            ioException -> {
                caught.set(true);
            },
            socket -> () -> {
                throw new IllegalStateException(
                    "IOException should've occurred in this test."
                );
            }
        ).start();
        MatcherAssert.assertThat(
            caught.get(),
            CoreMatchers.equalTo(true)
        );
    }

}
