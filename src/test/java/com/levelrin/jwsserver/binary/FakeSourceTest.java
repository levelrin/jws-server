/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.binary;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests.
 */
final class FakeSourceTest {

    @Test
    public void shouldReturnBitsOneAfterAnother() {
        final BinarySource source = new FakeSource(
            "0",
            "01",
            "111"
        );
        final List<String> expected = new ArrayList<>();
        // length does not matter.
        expected.add(source.nextBits(0));
        expected.add(source.nextBits(0));
        expected.add(source.nextBits(0));
        MatcherAssert.assertThat(
            expected,
            Matchers.containsInRelativeOrder(
                "0",
                "01",
                "111"
            )
        );
    }

}
