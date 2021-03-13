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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Tests.
 */
final class WithThreadTest {

    @Test
    public void shouldCreateNewThread() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final ExecutorService executorService = Executors.newCachedThreadPool();
        new WithThread(
            executorService,
            latch::countDown
        ).start();
        final int timeout = 100;
        if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
            throw new IllegalStateException("It didn't execute the encapsulated object.");
        }
        MatcherAssert.assertThat(
            ((ThreadPoolExecutor) executorService).getCompletedTaskCount(),
            CoreMatchers.equalTo(1L)
        );
    }

}
