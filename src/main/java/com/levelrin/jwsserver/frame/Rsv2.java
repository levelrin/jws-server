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
 * It represents the RSV2 bit.
 */
public final class Rsv2 implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We can do something with the RSV2 bit using this.
     */
    @SuppressWarnings("MemberName")
    private final Function<Byte, FrameSection> onRsv2;

    /**
     * Constructor.
     * @param source See {@link Rsv2#source}.
     * @param onRsv2 See {@link Rsv2#onRsv2}.
     */
    @SuppressWarnings("ParameterName")
    public Rsv2(final BinarySource source, final Function<Byte, FrameSection> onRsv2) {
        this.source = source;
        this.onRsv2 = onRsv2;
    }

    @Override
    public FrameSection next() {
        final String bit = this.source.nextBits(1);
        final FrameSection nextSection;
        if (bit.length() == 1) {
            nextSection = this.onRsv2.apply(Byte.parseByte(bit));
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
