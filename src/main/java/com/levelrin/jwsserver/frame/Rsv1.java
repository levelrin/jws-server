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
 * It represents the RSV1 bit.
 */
public final class Rsv1 implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We can do something with the RSV1 bit using this.
     */
    @SuppressWarnings("MemberName")
    private final Function<Byte, FrameSection> onRsv1;

    /**
     * Constructor.
     * @param source See {@link Rsv1#source}.
     * @param onRsv1 See {@link Rsv1#onRsv1}.
     */
    @SuppressWarnings("ParameterName")
    public Rsv1(final BinarySource source, final Function<Byte, FrameSection> onRsv1) {
        this.source = source;
        this.onRsv1 = onRsv1;
    }

    @Override
    public FrameSection next() {
        final String bit = this.source.nextBits(1);
        final FrameSection nextSection;
        if (bit.length() == 1) {
            nextSection = this.onRsv1.apply(Byte.parseByte(bit));
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
