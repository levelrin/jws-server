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
 * It represents the FIN bit of the WebSocket frame.
 */
public final class Fin implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We can do something with the FIN bit using this.
     */
    private final Function<Byte, FrameSection> onFin;

    /**
     * Constructor.
     * @param source See {@link Fin#source}.
     * @param onFin See {@link Fin#onFin}.
     */
    public Fin(final BinarySource source, final Function<Byte, FrameSection> onFin) {
        this.source = source;
        this.onFin = onFin;
    }

    @Override
    public FrameSection next() {
        final String bit = this.source.nextBits(1);
        final FrameSection nextSection;
        if (bit.length() == 1) {
            nextSection = this.onFin.apply(Byte.parseByte(bit));
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
