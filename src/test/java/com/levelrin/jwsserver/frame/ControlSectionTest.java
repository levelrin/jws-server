/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import com.levelrin.jwsserver.frame.control.FrameControl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Tests.
 */
final class ControlSectionTest {

    @Test
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
    public void shouldReturnMatchedFrameControl() {
        final String opcode = "1111";
        final FrameSection section = Mockito.mock(FrameSection.class);
        final FrameControl control = Mockito.mock(FrameControl.class);
        Mockito.doReturn(true).when(control).match(opcode);
        Mockito.doReturn(section).when(control).section();
        MatcherAssert.assertThat(
            new ControlSection(
                opcode,
                Collections.singletonList(control)
            ).next(),
            CoreMatchers.equalTo(section)
        );
    }

    @Test
    public void shouldReturnStopFramingIfNothingWasMatched() {
        MatcherAssert.assertThat(
            new ControlSection(
                "",
                new ArrayList<>()
            ).next(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldContinue() {
        MatcherAssert.assertThat(
            new ControlSection(
                "",
                new ArrayList<>()
            ).shouldContinue(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldNotEnd() {
        MatcherAssert.assertThat(
            new ControlSection(
                "",
                new ArrayList<>()
            ).isEnd(),
            CoreMatchers.equalTo(false)
        );
    }

}
