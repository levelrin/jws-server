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
 * It represents the payload data of the WebSocket frame.
 * Although the size of payload data can be extremely large according to RFC 6455,
 * we do not support the size beyond the capacity of a byte array
 * because we guess it's not a realistic scenario
 * that someone sends a massive data in a frame.
 */
public final class PayloadData implements FrameSection {

    /**
     * We will obtain the binary data from this.
     */
    private final BinarySource source;

    /**
     * Payload length in byte.
     */
    private final int payloadLength;

    /**
     * We can do something with the payload data using this.
     * The first parameter represents the payload data in bytes.
     * Note, we assume that the data is encoded.
     * We will decode the data using the masking-key.
     */
    private final Function<byte[], FrameSection> onData;

    /**
     * Constructor.
     * @param source See {@link PayloadData#source}.
     * @param payloadLength See {@link PayloadData#payloadLength}.
     * @param onData See {@link PayloadData#onData}.
     */
    public PayloadData(final BinarySource source, final int payloadLength, final Function<byte[], FrameSection> onData) {
        this.source = source;
        this.payloadLength = payloadLength;
        this.onData = onData;
    }

    @Override
    public FrameSection next() {
        final byte[] data = new byte[this.payloadLength];
        boolean issue = false;
        for (int index = 0; index < this.payloadLength; index = index + 1) {
            final int eightBits = 8;
            final String nextBits = this.source.nextBits(eightBits);
            if (nextBits.length() == eightBits) {
                data[index] = (byte) Short.parseShort(nextBits, 2);
            } else {
                issue = true;
                break;
            }
        }
        final FrameSection nextSection;
        if (issue) {
            nextSection = new StopFraming();
        } else {
            nextSection = this.onData.apply(data);
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
