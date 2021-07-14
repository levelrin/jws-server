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
final class MaskingKeyTest {

    @Test
    @SuppressWarnings("MagicNumber")
    public void shouldConvert32BitsIntoByteArray() {
        final String nextBits = "01101111110110001101110011011001";
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn(nextBits).when(source).nextBits(Mockito.anyInt());
        final AtomicReference<byte[]> actual = new AtomicReference<>();
        new MaskingKey(
            source,
            key -> {
                actual.set(key);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        MatcherAssert.assertThat(
            actual.get(),
            CoreMatchers.equalTo(new byte[] {
                111, -40, -36, -39,
            })
        );
    }

    @Test
    public void shouldReturnStopFramingIfNextBitsAreNot32Bits() {
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("").when(source).nextBits(Mockito.anyInt());
        MatcherAssert.assertThat(
            new MaskingKey(
                source,
                key -> Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new MaskingKey(
                Mockito.mock(BinarySource.class),
                key -> Mockito.mock(StopFraming.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new MaskingKey(
                Mockito.mock(BinarySource.class),
                key -> Mockito.mock(StopFraming.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
