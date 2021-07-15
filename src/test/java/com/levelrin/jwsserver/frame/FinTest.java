/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import com.levelrin.jwsserver.binary.BinarySource;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class FinTest {

    @Test
    public void shouldReadOneBitFromSource() {
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("1").when(source).nextBits(Mockito.anyInt());
        final AtomicReference<Byte> actual = new AtomicReference<>();
        new Fin(
            source,
            bit -> {
                actual.set(bit);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        MatcherAssert.assertThat(
            actual.get(),
            CoreMatchers.equalTo((byte) 1)
        );
    }

    @Test
    public void shouldReturnStopFramingIfSourceDidNotReturnOneBit() {
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("").when(source).nextBits(Mockito.anyInt());
        MatcherAssert.assertThat(
            new Fin(
                source,
                bit -> Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new Fin(
                Mockito.mock(BinarySource.class),
                bit -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new Fin(
                Mockito.mock(BinarySource.class),
                bit -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
