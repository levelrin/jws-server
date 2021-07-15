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
final class OpcodeTest {

    @Test
    public void shouldReadFourBitsFromSource() {
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("1111").when(source).nextBits(Mockito.anyInt());
        final AtomicReference<String> actual = new AtomicReference<>();
        new Opcode(
            source,
            bits -> {
                actual.set(bits);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        MatcherAssert.assertThat(
            actual.get(),
            CoreMatchers.equalTo("1111")
        );
    }

    @Test
    public void shouldReturnStopFramingIfSourceDidNotReturnFourBits() {
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("").when(source).nextBits(Mockito.anyInt());
        MatcherAssert.assertThat(
            new Opcode(
                source,
                bits -> Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new Opcode(
                Mockito.mock(BinarySource.class),
                bits -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new Opcode(
                Mockito.mock(BinarySource.class),
                bits -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
