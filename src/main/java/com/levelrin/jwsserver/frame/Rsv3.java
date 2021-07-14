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
 * It represents the RSV3 bit.
 */
public final class Rsv3 implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We can do something with the RSV3 bit using this.
     */
    @SuppressWarnings("MemberName")
    private final Function<Byte, FrameSection> onRsv3;

    /**
     * Constructor.
     * @param source See {@link Rsv3#source}.
     * @param onRsv3 See {@link Rsv3#onRsv3}.
     */
    @SuppressWarnings("ParameterName")
    public Rsv3(final BinarySource source, final Function<Byte, FrameSection> onRsv3) {
        this.source = source;
        this.onRsv3 = onRsv3;
    }

    @Override
    public FrameSection next() {
        final String bit = this.source.nextBits(1);
        final FrameSection nextSection;
        if (bit.length() == 1) {
            nextSection = this.onRsv3.apply(Byte.parseByte(bit));
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
