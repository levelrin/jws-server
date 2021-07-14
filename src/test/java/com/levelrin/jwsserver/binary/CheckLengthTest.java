/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.binary;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests.
 */
final class CheckLengthTest {

    @Test
    public void shouldThrowExceptionIfLengthIsLessThanOne() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new CheckLength(
                Mockito.mock(BinarySource.class)
            ).nextBits(0),
            "The length of the nextBits must be greater than 0."
        );
    }

    @Test
    public void shouldUseOriginIfLengthIsGreaterThanZero() {
        final BinarySource origin = Mockito.mock(BinarySource.class);
        Mockito.doReturn("0101").when(origin).nextBits(Mockito.anyInt());
        MatcherAssert.assertThat(
            new CheckLength(origin).nextBits(1),
            CoreMatchers.equalTo("0101")
        );
    }

}
