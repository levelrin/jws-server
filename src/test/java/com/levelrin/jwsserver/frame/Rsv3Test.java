/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import com.levelrin.jwsserver.binary.BinarySource;
import com.levelrin.jwsserver.binary.FakeSource;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class Rsv3Test {

    @Test
    public void shouldReadFirstBit() {
        final AtomicReference<Byte> actual = new AtomicReference<>();
        new Rsv3(
            new FakeSource("0"),
            rsv -> {
                actual.set(rsv);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        MatcherAssert.assertThat(
            actual.get(),
            CoreMatchers.equalTo((byte) 0)
        );
    }

    @Test
    public void shouldReturnStopFramingIfSourceDidNotGiveOneBit() {
        MatcherAssert.assertThat(
            new Rsv3(
                new FakeSource("01"),
                rsv -> Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new Rsv3(
                Mockito.mock(BinarySource.class),
                rsv -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new Rsv3(
                Mockito.mock(BinarySource.class),
                rsv -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
