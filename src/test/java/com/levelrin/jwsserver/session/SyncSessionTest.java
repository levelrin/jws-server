/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Tests.
 */
final class SyncSessionTest {

    @Test
    public void idShouldComeFromOrigin() {
        final Session origin = Mockito.mock(Session.class);
        Mockito.doReturn("Yoi").when(origin).id();
        MatcherAssert.assertThat(
            new SyncSession(origin).id(),
            CoreMatchers.equalTo("Yoi")
        );
    }

    @Test
    public void originShouldSendTextMessage() {
        final Session origin = Mockito.mock(Session.class);
        new SyncSession(origin).sendMessage("Hi");
        Mockito.verify(origin).sendMessage("Hi");
    }

    @Test
    public void originShouldSendBinaryMessage() {
        final Session origin = Mockito.mock(Session.class);
        new SyncSession(origin).sendMessage(
            "bytes".getBytes(StandardCharsets.UTF_8)
        );
        Mockito.verify(origin).sendMessage(
            "bytes".getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    public void originShouldClose() {
        final Session origin = Mockito.mock(Session.class);
        new SyncSession(origin).close();
        Mockito.verify(origin).close();
    }

    @Test
    public void sessionShouldBeThreadSafe() throws InterruptedException {
        final Count count = new Count();
        final Session session = new SyncSession(
            new CountSession(count)
        );
        final ExecutorService service = Executors.newCachedThreadPool();
        final int repeat = 10_000;
        for (int iteration = 0; iteration < repeat; iteration = iteration + 1) {
            service.execute(session::id);
            service.execute(() -> session.sendMessage("One"));
            service.execute(() -> session.sendMessage("Two".getBytes(StandardCharsets.UTF_8)));
            service.execute(session::close);
        }
        service.shutdown();
        final int timeout = 5;
        if (!service.awaitTermination(timeout, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Threads could not be finished within the timeout.");
        }
        final int expectedCount = 40_000;
        MatcherAssert.assertThat(
            count.value(),
            CoreMatchers.equalTo(expectedCount)
        );
    }

    /**
     * It's a simple non-thread-safe counter.
     * We will use this to test the thread safety.
     */
    private static final class Count {

        /**
         * The initial value is 0.
         */
        private int val;

        /**
         * Increment the counter by 1.
         */
        public void increment() {
            this.val = this.val + 1;
        }

        /**
         * We will use this to assert that this is the same as expected counter value.
         * @return Current value of the counter.
         */
        public int value() {
            return this.val;
        }

    }

    /**
     * We will decorate this object.
     * It will simply increment the non-thread-safe counter
     * when it calls any method.
     */
    private static final class CountSession implements Session {

        /**
         * We will increment this counter.
         */
        private final Count count;

        /**
         * Constructor.
         * @param count See {@link CountSession#count}.
         */
        CountSession(final Count count) {
            this.count = count;
        }

        @Override
        @SuppressWarnings("PMD.ShortMethodName")
        public String id() {
            this.count.increment();
            return "";
        }

        @Override
        public void sendMessage(final String message) {
            this.count.increment();
        }

        @Override
        public void sendMessage(final byte[] message) {
            this.count.increment();
        }

        @Override
        public void close() {
            this.count.increment();
        }

    }

}
