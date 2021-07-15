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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class PayloadDataTest {

    @Test
    public void shouldReadNextBitsByPayloadLength() {
        final int payloadLength = 3;
        final BinarySource source = new FakeSource(
            "01011001",
            "01101111",
            "01101001"
        );
        final AtomicReference<String> actual = new AtomicReference<>();
        new PayloadData(
            source,
            payloadLength,
            payload -> {
                actual.set(new String(payload, StandardCharsets.UTF_8));
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        MatcherAssert.assertThat(
            actual.get(),
            CoreMatchers.equalTo("Yoi")
        );
    }

    @Test
    public void shouldReturnStopFramingIfNextBitsAreNot8Bits() {
        final BinarySource source = Mockito.mock(BinarySource.class);
        Mockito.doReturn("0101010101").when(source).nextBits(Mockito.anyInt());
        MatcherAssert.assertThat(
            new PayloadData(
                source,
                1,
                payload -> Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new PayloadData(
                Mockito.mock(BinarySource.class),
                1,
                payload -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new PayloadData(
                Mockito.mock(BinarySource.class),
                1,
                payload -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
