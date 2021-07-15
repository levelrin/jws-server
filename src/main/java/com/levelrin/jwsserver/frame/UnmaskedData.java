/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import java.util.function.Function;

/**
 * It represents the unmasked payload data of the WebSocket frame.
 */
public final class UnmaskedData implements FrameSection {

    /**
     * Masking-key.
     */
    private final byte[] key;

    /**
     * Payload data.
     */
    private final byte[] data;

    /**
     * We can do something with the unmasked payload data using this.
     * The first parameter represents the unmasked payload data in bytes.
     */
    private final Function<byte[], FrameSection> onUnmasked;

    /**
     * Constructor.
     * @param key See {@link UnmaskedData#key}.
     * @param data See {@link UnmaskedData#data}.
     * @param onUnmasked See {@link UnmaskedData#onUnmasked}.
     */
    public UnmaskedData(final byte[] key, final byte[] data, final Function<byte[], FrameSection> onUnmasked) {
        this.key = key.clone();
        this.data = data.clone();
        this.onUnmasked = onUnmasked;
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public FrameSection next() {
        final byte[] decoded = new byte[this.data.length];
        for (int index = 0; index < this.data.length; index = index + 1) {
            decoded[index] = (byte) (this.data[index] ^ this.key[index % 4]);
        }
        return this.onUnmasked.apply(decoded);
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
