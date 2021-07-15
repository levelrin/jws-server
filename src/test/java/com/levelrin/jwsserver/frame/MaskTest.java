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

/**
 * Tests.
 */
final class MaskTest {

    @Test
    public void shouldUseOriginIfNextBitIsOne() {
        final FrameSection origin = Mockito.mock(FrameSection.class);
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("1").when(source).nextBits(Mockito.anyInt());
        MatcherAssert.assertThat(
            new Mask(
                source,
                origin
            ).next(),
            CoreMatchers.equalTo(origin)
        );
    }

    @Test
    public void shouldReturnStopFramingIfNextBitIsNotOne() {
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("").when(source).nextBits(Mockito.anyInt());
        MatcherAssert.assertThat(
            new Mask(
                source,
                Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new Mask(
                Mockito.mock(BinarySource.class),
                Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new Mask(
                Mockito.mock(BinarySource.class),
                Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
