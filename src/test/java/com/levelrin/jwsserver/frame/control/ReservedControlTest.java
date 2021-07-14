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
final class ReservedControlTest {

    @Test
    public void shouldMatchOpcodeForReservedControlFrame() {
        MatcherAssert.assertThat(
            new ReservedControl().match("1011"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedControl().match("1100"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedControl().match("1101"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedControl().match("1110"),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            new ReservedControl().match("1111"),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotMatchOpcodeOtherThanReservedControlFrame() {
        MatcherAssert.assertThat(
            new ReservedControl().match("0000"),
            CoreMatchers.equalTo(false)
        );
    }

}
