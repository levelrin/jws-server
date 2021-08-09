/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

/**
 * Let's say the client wants to send a text message via multiple fragments.
 * In such a case, we need to append data from each fragment to get the full payload.
 * It's responsible for saving the data.
 * It will append the next data if the data is already stored.
 */
public final class OnSavePayload implements FrameSection {

    /**
     * The payload of the current frame.
     * We will store this.
     * It should be unmasked data.
     */
    private final byte[] payload;

    /**
     * We will store the payload into this.
     */
    private final ByteArrayOutputStream cache;

    /**
     * The first parameter is the payload from the cache.
     * In other words, it can be a payload via multiple fragments.
     * The return value is the next frame using the cached payload.
     * We assume that the cache will be cleared by the next frame
     * if the FIN bit is 1, which indicates the last fragment.
     */
    private final Function<byte[], FrameSection> fromCache;

    /**
     * Constructor.
     * @param payload See {@link OnSavePayload#payload}.
     * @param cache See {@link OnSavePayload#cache}.
     * @param fromCache See {@link OnSavePayload#fromCache}.
     */
    public OnSavePayload(final byte[] payload, final ByteArrayOutputStream cache, final Function<byte[], FrameSection> fromCache) {
        this.payload = payload.clone();
        this.cache = cache;
        this.fromCache = fromCache;
    }

    @Override
    public FrameSection next() {
        try {
            this.cache.write(this.payload);
            return this.fromCache.apply(this.cache.toByteArray()).next();
        } catch (final IOException ioException) {
            throw new IllegalStateException("Failed to store the payload.", ioException);
        }
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
