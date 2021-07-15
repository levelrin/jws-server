/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class UnmaskedDataTest {

    @Test
    @SuppressWarnings("MagicNumber")
    public void shouldUnmaskPayloadData() {
        final AtomicReference<byte[]> actual = new AtomicReference<>();
        new UnmaskedData(
            new byte[] {
                (byte) 167, (byte) 225, (byte) 225, (byte) 210,
            },
            new byte[] {
                (byte) 198, (byte) 131, (byte) 130, (byte) 182, (byte) 194, (byte) 135,
            },
            unmasked -> {
                actual.set(unmasked);
                return Mockito.mock(FrameSection.class);
            }
        ).next();
        MatcherAssert.assertThat(
            new String(actual.get(), StandardCharsets.UTF_8),
            CoreMatchers.equalTo("abcdef")
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new UnmaskedData(
                new byte[1],
                new byte[1],
                unmasked -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new UnmaskedData(
                new byte[1],
                new byte[1],
                unmasked -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
