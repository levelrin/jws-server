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
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests.
 */
@SuppressWarnings("PMD.CloseResource")
final class OnWriterTest {

    @Test
    public void shouldCreateWriterFromSocket() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        Mockito.doReturn(Mockito.mock(OutputStream.class)).when(socket).getOutputStream();
        new OnWriter(
            socket,
            ioException -> {
                throw new IllegalStateException("Exception should not be caught in this test.");
            },
            writer -> Mockito.mock(WsServer.class)
        ).start();
        Mockito.verify(socket).getOutputStream();
    }

    @Test
    public void shouldCatchIoException() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        Mockito.doThrow(IOException.class).when(socket).getOutputStream();
        final AtomicBoolean caught = new AtomicBoolean(false);
        new OnWriter(
            socket,
            ioException -> caught.set(true),
            writer -> Mockito.mock(WsServer.class)
        ).start();
        MatcherAssert.assertThat(
            caught.get(),
            CoreMatchers.equalTo(true)
        );
    }

}
