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
final class CloseControlTest {

    @Test
    public void shouldMatchOpcodeForCloseFrame() {
        MatcherAssert.assertThat(
            new CloseControl().match("1000"),
            CoreMatchers.equalTo(true)
        );
    }

}
