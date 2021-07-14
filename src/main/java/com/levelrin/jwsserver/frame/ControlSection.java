/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import com.levelrin.jwsserver.frame.control.FrameControl;
import java.util.List;

/**
 * It's responsible for checking the opcode and navigate us to the appropriate next section.
 */
public final class ControlSection implements FrameSection {

    /**
     * We will check this to see what kind of frame we are dealing with.
     */
    private final String opcode;

    /**
     * We will iterate this to find the appropriate next section.
     */
    private final List<FrameControl> controls;

    /**
     * Constructor.
     * @param opcode See {@link ControlSection#opcode}.
     * @param controls See {@link ControlSection#controls}.
     */
    public ControlSection(final String opcode, final List<FrameControl> controls) {
        this.opcode = opcode;
        this.controls = controls;
    }

    @Override
    public FrameSection next() {
        FrameSection section = new StopFraming();
        for (final FrameControl control : this.controls) {
            if (control.match(this.opcode)) {
                section = control.section();
                break;
            }
        }
        return section;
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public boolean isEnd() {
        return false;
    }

}
