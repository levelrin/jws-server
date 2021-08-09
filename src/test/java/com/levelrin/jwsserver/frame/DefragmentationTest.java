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
final class DefragmentationTest {

    @Test
    public void nextSectionShouldBeEndIfFragmentIsContinuousFrame() {
        MatcherAssert.assertThat(
            new Defragmentation(
                (byte) 0,
                new ByteArrayOutputStream(),
                Mockito.mock(FrameSection.class)
            ).next(),
            CoreMatchers.instanceOf(End.class)
        );
    }

    @Test
    public void nextSectionShouldComeFromOriginIfFrameIsLastFragment() {
        final FrameSection origin = Mockito.mock(FrameSection.class);
        final FrameSection next = Mockito.mock(FrameSection.class);
        Mockito.doReturn(next).when(origin).next();
        MatcherAssert.assertThat(
            new Defragmentation(
                (byte) 1,
                new ByteArrayOutputStream(),
                origin
            ).next(),
            CoreMatchers.equalTo(next)
        );
    }

    @Test
    public void shouldClearCacheIfFrameIsLastFragment() {
        final FrameSection origin = Mockito.mock(FrameSection.class);
        final FrameSection next = Mockito.mock(FrameSection.class);
        Mockito.doReturn(next).when(origin).next();
        final ByteArrayOutputStream cache = Mockito.mock(ByteArrayOutputStream.class);
        new Defragmentation(
            (byte) 1,
            cache,
            origin
        ).next();
        Mockito.verify(cache).reset();
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new Defragmentation(
                (byte) 0,
                Mockito.mock(ByteArrayOutputStream.class),
                Mockito.mock(FrameSection.class)
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotBeEnd() {
        MatcherAssert.assertThat(
            new Defragmentation(
                (byte) 0,
                Mockito.mock(ByteArrayOutputStream.class),
                Mockito.mock(FrameSection.class)
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
