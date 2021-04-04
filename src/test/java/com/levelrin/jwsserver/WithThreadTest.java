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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests.
 */
final class WithThreadTest {

    @Test
    public void shouldCreateNewThread() throws InterruptedException {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final AtomicBoolean executed = new AtomicBoolean(false);
        new WithThread(
            executorService,
            () -> executed.set(true)
        ).start();
        final int timeout = 100;
        executorService.shutdown();
        final boolean terminated = executorService.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        if (!terminated) {
            throw new IllegalStateException(
                "The encapsulated object was not able to finish its task before the timeout."
            );
        }
        MatcherAssert.assertThat(
            executed.get(),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            ((ThreadPoolExecutor) executorService).getCompletedTaskCount(),
            CoreMatchers.equalTo(1L)
        );
    }

}
