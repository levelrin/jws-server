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
import java.io.ByteArrayOutputStream;

/**
 * Tests.
 */
final class OnSavePayloadTest {

    @Test
    @SuppressWarnings("MagicNumber")
    public void shouldSavePayload() {
        final ByteArrayOutputStream cache = new ByteArrayOutputStream();
        new OnSavePayload(
            new byte[] {
                0, 1, 3,
            },
            cache,
            cachedData -> Mockito.mock(FrameSection.class)
        ).next();
        MatcherAssert.assertThat(
            cache.toByteArray(),
            CoreMatchers.equalTo(
                new byte[] {
                    0, 1, 3,
                }
            )
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new OnSavePayload(
                new byte[0],
                Mockito.mock(ByteArrayOutputStream.class),
                cachedData -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotBeEnd() {
        MatcherAssert.assertThat(
            new OnSavePayload(
                new byte[0],
                Mockito.mock(ByteArrayOutputStream.class),
                cachedData -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
