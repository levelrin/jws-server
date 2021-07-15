/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.FrameSection;

/**
 * It checks if the opcode denotes a reserved non-control frame.
 */
public final class ReservedNonControl implements FrameControl {

    @Override
    public boolean match(final String opcode) {
        return switch (opcode) {
            case "0011", "0100", "0101", "0110", "0111" -> true;
            default -> false;
        };
    }

    @Override
    public FrameSection section() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
