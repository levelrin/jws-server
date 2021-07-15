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
 * It represents the masking-key of the WebSocket frame.
 * Since all frames sent from the client to the server must be masked,
 * it assumes that the MASK bit was 1.
 */
public final class MaskingKey implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We can do something with the masking key using this.
     * The first parameter represents the masking key in bytes.
     */
    private final Function<byte[], FrameSection> onKey;

    /**
     * Constructor.
     * @param source See {@link MaskingKey#source}.
     * @param onKey See {@link MaskingKey#onKey}.
     */
    public MaskingKey(final BinarySource source, final Function<byte[], FrameSection> onKey) {
        this.source = source;
        this.onKey = onKey;
    }

    @Override
    public FrameSection next() {
        final int length = 32;
        final String bits = this.source.nextBits(length);
        final FrameSection nextSection;
        if (bits.length() == length) {
            // Split the entire binary numbers into 8 bits array.
            final String[] eightChars = bits.split("(?<=\\G........)");
            final byte keySize = 4;
            final byte[] bytes = new byte[keySize];
            for (int index = 0; index < bytes.length; index = index + 1) {
                // The type casting may alter the number, but it's okay in this case.
                bytes[index] = (byte) Integer.parseInt(eightChars[index], 2);
            }
            nextSection = this.onKey.apply(bytes);
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
