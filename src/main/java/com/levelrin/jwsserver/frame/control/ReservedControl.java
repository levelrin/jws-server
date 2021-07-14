/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.FrameSection;

/**
 * It checks if the opcode denotes a reserved control frame.
 */
public final class ReservedControl implements FrameControl {

    @Override
    public boolean match(final String opcode) {
        return switch (opcode) {
            case "1011", "1100", "1101", "1110", "1111" -> true;
            default -> false;
        };
    }

    @Override
    public FrameSection section() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
