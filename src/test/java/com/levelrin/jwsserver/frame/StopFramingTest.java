/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests.
 */
final class StopFramingTest {

    @Test
    public void shouldThrowExceptionOnNext() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new StopFraming().next()
        );
    }

    @Test
    public void shouldNotContinue() {
        MatcherAssert.assertThat(
            new StopFraming().shouldContinue(),
            CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldEnd() {
        MatcherAssert.assertThat(
            new StopFraming().isEnd(),
            CoreMatchers.equalTo(true)
        );
    }

}
