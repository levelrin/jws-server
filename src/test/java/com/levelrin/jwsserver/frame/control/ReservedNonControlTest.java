/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests.
 */
final class ReservedNonControlTest {

    @Test
    public void shouldMatchOpcodeForNonControlFrame() {
        MatcherAssert.assertThat(
            new ReservedNonControl().match("0011"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedNonControl().match("0100"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedNonControl().match("0101"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedNonControl().match("0110"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedNonControl().match("0111"),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotMatchOpcodeOtherThanNonControlFrame() {
        MatcherAssert.assertThat(
            new ReservedNonControl().match("0000"),
            CoreMatchers.equalTo(false)
        );
    }

}
