/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.frame.FrameSection;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests.
 */
final class WithFramingTest {

    /**
     * Timeout for each test case.
     */
    private static final long TIME_OUT = 3000L;

    @Test
    @Timeout(value = TIME_OUT, unit = TimeUnit.MILLISECONDS)
    public void shouldContinueCallingNextUntilItShouldNot() {
        final AtomicInteger firstCount = new AtomicInteger(0);
        final AtomicInteger secondCount = new AtomicInteger(0);
        final FrameSection frame = new First(
            firstCount,
            new Second(
                secondCount,
                new Third()
            )
        );
        new WithFraming(frame).start();
        MatcherAssert.assertThat(
            firstCount.get(),
            CoreMatchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            secondCount.get(),
            CoreMatchers.equalTo(1)
        );
    }

    @Test
    @Timeout(value = TIME_OUT, unit = TimeUnit.MILLISECONDS)
    public void shouldRepeatFromStartIfItReachEnd() {
        final AtomicInteger firstCount = new AtomicInteger(0);
        final AtomicInteger secondCount = new AtomicInteger(0);
        final FrameSection frame = new First(
            firstCount,
            new Second(
                secondCount,
                new Repeat(1)
            )
        );
        new WithFraming(frame).start();
        final int expectedCall = 2;
        MatcherAssert.assertThat(
            firstCount.get(),
            CoreMatchers.equalTo(expectedCall)
        );
        MatcherAssert.assertThat(
            secondCount.get(),
            CoreMatchers.equalTo(expectedCall)
        );
    }

    /**
     * Initial frame section.
     * It should continue to next.
     */
    private static final class First implements FrameSection {

        /**
         * It tells us how many times it called the next frame section.
         */
        private final AtomicInteger count;

        /**
         * This will be the next frame section.
         */
        private final FrameSection origin;

        /**
         * Constructor.
         * @param count See {@link First#count}.
         * @param origin See {@link First#origin}.
         */
        First(final AtomicInteger count, final FrameSection origin) {
            this.count = count;
            this.origin = origin;
        }

        @Override
        public FrameSection next() {
            this.count.incrementAndGet();
            return this.origin;
        }

        @Override
        public boolean shouldContinue() {
            return true;
        }

        @Override
        public boolean isEnd() {
            return false;
        }

    }

    /**
     * Second frame section.
     * It's the same as {@link First}.
     */
    private static final class Second implements FrameSection {

        /**
         * Same as {@link First#count}.
         */
        private final AtomicInteger count;

        /**
         * Same as {@link First#origin}.
         */
        private final FrameSection origin;

        /**
         * Constructor.
         * @param count See {@link Second#count}.
         * @param origin See {@link Second#origin}.
         */
        Second(final AtomicInteger count, final FrameSection origin) {
            this.count = count;
            this.origin = origin;
        }

        @Override
        public FrameSection next() {
            this.count.incrementAndGet();
            return this.origin;
        }

        @Override
        public boolean shouldContinue() {
            return true;
        }

        @Override
        public boolean isEnd() {
            return false;
        }

    }

    /**
     * Third frame section.
     * It should not continue to the next frame section.
     * It will throw an exception if it attempts to call next.
     */
    private static final class Third implements FrameSection {

        @Override
        public FrameSection next() {
            throw new UnsupportedOperationException("This method should not be called.");
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public boolean isEnd() {
            return true;
        }

    }

    /**
     * It's the end of the frame section.
     * It should continue as much as we specify the repeat-amount.
     * Continue means we will use the initial frame section again.
     * Discontinue means we will stop framing.
     * It will throw an exception if it attempts to call next.
     * It will throw an exception if it attempts to repeat more than we specify.
     */
    private static final class Repeat implements FrameSection {

        /**
         * Amount of repeat.
         * 0 means no repeat.
         * 1 means repeat once.
         */
        private final int amount;

        /**
         * It's for counting how many times this object is used.
         */
        private final AtomicInteger count = new AtomicInteger(0);

        /**
         * Constructor.
         * @param amount See {@link Repeat#amount}.
         */
        Repeat(final int amount) {
            this.amount = amount;
        }

        @Override
        public FrameSection next() {
            throw new UnsupportedOperationException("This method should not be called.");
        }

        @Override
        public boolean shouldContinue() {
            final boolean result;
            if (this.count.get() < this.amount) {
                this.count.getAndIncrement();
                result = true;
            } else if (this.count.get() == this.amount) {
                result = false;
            } else {
                throw new IllegalStateException("It's trying to repeat more than we specified.");
            }
            return result;
        }

        @Override
        public boolean isEnd() {
            return true;
        }

    }

}
