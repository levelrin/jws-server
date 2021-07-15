/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import com.levelrin.jwsserver.binary.BinarySource;

/**
 * It represents the MASK bit of the WebSocket frame.
 */
public final class Mask implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We will use this if the mask bit is 1.
     * Since all frames sent from the client to the server must be masked,
     * we will stop the framing if the mask bit is 0.
     */
    private final FrameSection origin;

    /**
     * Constructor.
     * @param source See {@link Mask#source}.
     * @param origin See {@link Mask#origin}.
     */
    public Mask(final BinarySource source, final FrameSection origin) {
        this.source = source;
        this.origin = origin;
    }

    @Override
    public FrameSection next() {
        final String bit = this.source.nextBits(1);
        final FrameSection nextSection;
        if ("1".equals(bit)) {
            nextSection = this.origin;
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
