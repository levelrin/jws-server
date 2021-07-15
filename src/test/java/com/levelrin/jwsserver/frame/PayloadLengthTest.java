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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests.
 */
final class PayloadLengthTest {

    @Test
    public void first7bitsShouldBePayloadLengthIfValueWas0To125() {
        final AtomicInteger payloadLength = new AtomicInteger(0);
        new PayloadLength(
            new FakeSource("1111011"),
            length -> {
                payloadLength.set(length);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        final int expected = 123;
        MatcherAssert.assertThat(
            payloadLength.get(),
            CoreMatchers.equalTo(expected)
        );
    }

    @Test
    public void following2bytesShouldBePayloadLengthIfFirst7BitsValueWas126() {
        final AtomicInteger payloadLength = new AtomicInteger(0);
        new PayloadLength(
            new FakeSource(
                "1111110",
                "1101101001010000"
            ),
            length -> {
                payloadLength.set(length);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        final int expected = 55_888;
        MatcherAssert.assertThat(
            payloadLength.get(),
            CoreMatchers.equalTo(expected)
        );
    }

    @Test
    public void following8bytesShouldBePayloadLengthIfFirst8BitsValueWas127() {
        final AtomicInteger payloadLength = new AtomicInteger(0);
        new PayloadLength(
            new FakeSource(
                "1111111",
                "0000000000000000000000000000000000011100110001110110101010110011"
            ),
            length -> {
                payloadLength.set(length);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        final int expected = 482_831_027;
        MatcherAssert.assertThat(
            payloadLength.get(),
            CoreMatchers.equalTo(expected)
        );
    }

    @Test
    public void shouldReturnStopFramingIfFirstReadWasNot7bits() {
        MatcherAssert.assertThat(
            new PayloadLength(
                new FakeSource(""),
                length -> Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new PayloadLength(
                Mockito.mock(BinarySource.class),
                length -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new PayloadLength(
                Mockito.mock(BinarySource.class),
                length -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
