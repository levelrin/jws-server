/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import com.levelrin.jwsserver.binary.BinarySource;
import java.util.function.Function;

/**
 * It represents the opcode of the WebSocket frame.
 */
public final class Opcode implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We can do something with the opcode using this.
     * The opcode is in String format.
     * The range of values are: 0000, 0001, 0010, ..., 1111.
     */
    private final Function<String, FrameSection> onOpcode;

    /**
     * Constructor.
     * @param source See {@link Opcode#source}.
     * @param onOpcode See {@link Opcode#onOpcode}.
     */
    public Opcode(final BinarySource source, final Function<String, FrameSection> onOpcode) {
        this.source = source;
        this.onOpcode = onOpcode;
    }

    @Override
    public FrameSection next() {
        final int opcodeSize = 4;
        final String bits = this.source.nextBits(opcodeSize);
        final FrameSection nextSection;
        if (bits.length() == opcodeSize) {
            nextSection = this.onOpcode.apply(bits);
        } else {
            nextSection = new StopFraming();
        }
        return nextSection;
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
