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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class OnSaveOpcodeTest {

    @Test
    public void nonContinuousOpcodeShouldBeStoredInCache() {
        final AtomicReference<String> cache = new AtomicReference<>();
        new OnSaveOpcode(
            // 0001 indicates a text frame
            "0001",
            cache,
            cachedOpcode -> Mockito.mock(FrameSection.class)
        ).next();
        MatcherAssert.assertThat(
            cache.get(),
            CoreMatchers.equalTo("0001")
        );
    }

    @Test
    public void continuousOpcodeShouldNotBeStoredInCache() {
        final AtomicReference<String> cache = new AtomicReference<>();
        new OnSaveOpcode(
            // 0000 indicates a continuous frame
            "0000",
            cache,
            cachedOpcode -> Mockito.mock(FrameSection.class)
        ).next();
        MatcherAssert.assertThat(
            cache.get(),
            CoreMatchers.not("0000")
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new OnSaveOpcode(
                "",
                new AtomicReference<>(),
                cachedOpcode -> Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotBeEnd() {
        MatcherAssert.assertThat(
            new OnSaveOpcode(
                "",
                new AtomicReference<>(),
                cachedOpcode -> Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
