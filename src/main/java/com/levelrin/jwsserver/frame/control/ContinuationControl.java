/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.FrameSection;

/**
 * It checks if the opcode denotes a continuation frame.
 */
public final class ContinuationControl implements FrameControl {

    @Override
    public boolean match(final String opcode) {
        return "0000".equals(opcode);
    }

    @Override
    public FrameSection section() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
