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
 * It represents the payload length of the WebSocket frame in byte.
 */
public final class PayloadLength implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * We can do something with the payload length using this.
     * The unit of payload length is a byte.
     */
    private final Function<Integer, FrameSection> onLength;

    /**
     * Constructor.
     * @param source See {@link PayloadLength#source}.
     * @param onLength See {@link PayloadLength#onLength}.
     */
    public PayloadLength(final BinarySource source, final Function<Integer, FrameSection> onLength) {
        this.source = source;
        this.onLength = onLength;
    }

    @Override
    public FrameSection next() {
        final int sevenBits = 7;
        final String firstBits = this.source.nextBits(sevenBits);
        final FrameSection nextSection;
        if (firstBits.length() == sevenBits) {
            final int firstLength = Integer.parseInt(firstBits, 2);
            final int nextLength = 126;
            final int maxLength = 127;
            if (firstLength < nextLength) {
                nextSection = this.onLength.apply(firstLength);
            } else if (firstLength == nextLength) {
                final int nextBits = 16;
                nextSection = this.nextSection(nextBits);
            } else if (firstLength == maxLength) {
                final int maxBits = 64;
                nextSection = this.nextSection(maxBits);
            } else {
                throw new IllegalStateException(
                    String.format(
                        "Programming error detected. The length of first bits were %d.",
                        firstLength
                    )
                );
            }
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

    /**
     * Read the next bits, calculate the payload length, and return the next section.
     * @param bitsLength The length of next bits from the source.
     * @return Next section with the payload length.
     */
    private FrameSection nextSection(final int bitsLength) {
        final String bits = this.source.nextBits(bitsLength);
        final FrameSection nextSection;
        if (bits.length() == bitsLength) {
            final int payloadLength = Integer.parseInt(bits, 2);
            nextSection = this.onLength.apply(payloadLength);
        } else {
            nextSection = new StopFraming();
        }
        return nextSection;
    }

}
