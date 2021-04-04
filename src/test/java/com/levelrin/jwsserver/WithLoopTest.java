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
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests.
 */
@SuppressWarnings("PMD.CloseResource")
final class WithLoopTest {

    @Test
    public void shouldConstantlyRunOriginUntilServerClosed() {
        final ServerSocket serverSocket = Mockito.mock(ServerSocket.class);
        Mockito.when(serverSocket.isClosed()).thenReturn(false);
        final AtomicInteger count = new AtomicInteger(0);
        new WithLoop(
            serverSocket,
            () -> {
                if (count.get() > 2) {
                    Mockito.when(serverSocket.isClosed()).thenReturn(true);
                } else {
                    count.incrementAndGet();
                }
            }
        ).start();
        final int runCount = 3;
        MatcherAssert.assertThat(
            count.get(),
            CoreMatchers.equalTo(runCount)
        );
    }

}
