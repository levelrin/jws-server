/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Let's say the client wants to send a text message via multiple fragments.
 * The first fragment will have the opcode to tell us that the message is in text.
 * The opcode of the next fragment will indicate that it's a continuous frame.
 * That means we need to remember the opcode from the first fragment to read
 * the message sent via multiple fragments correctly.
 * It's responsible for saving the opcode.
 * It will override the saved opcode when it encounters the new opcode
 * other than the continuous frame.
 */
public final class OnSaveOpcode implements FrameSection {

    /**
     * The opcode of the current frame.
     */
    private final String opcode;

    /**
     * We will store the opcode into this.
     */
    private final AtomicReference<String> opcodeCache;

    /**
     * The first parameter is the opcode from the cache.
     * The return value is the next section using the opcode from the cache.
     */
    private final Function<String, FrameSection> fromCache;

    /**
     * Constructor.
     * @param opcode See {@link OnSaveOpcode#opcode}.
     * @param opcodeCache See {@link OnSaveOpcode#opcodeCache}.
     * @param fromCache See {@link OnSaveOpcode#fromCache}.
     */
    public OnSaveOpcode(final String opcode, final AtomicReference<String> opcodeCache, final Function<String, FrameSection> fromCache) {
        this.opcode = opcode;
        this.opcodeCache = opcodeCache;
        this.fromCache = fromCache;
    }

    @Override
    public FrameSection next() {
        // 0000 indicates a continuous frame.
        if (!"0000".equals(this.opcode)) {
            this.opcodeCache.set(this.opcode);
        }
        return this.fromCache.apply(this.opcodeCache.get());
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
