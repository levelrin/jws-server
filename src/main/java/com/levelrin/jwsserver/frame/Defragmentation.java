/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

import java.io.ByteArrayOutputStream;

/**
 * The client may send a message via multiple fragments.
 * In that case, we should read multiple fragments before reacting to the message.
 * It's responsible for checking the FIN bit to determine whether the client
 * is sending a message via multiple fragments or not.
 * If the FIN bit is 0 (a fragment), we will return {@link End} as a next section.
 * That will make the system continuously read the next frame without reacting to the message.
 * Obviously, we have a data container to hold all the data from each fragment.
 * If the FIN bit is 1 (the final fragment in a message), we will return the next section
 * to react to the message. Additionally, we will clear the payload cache.
 */
public final class Defragmentation implements FrameSection {

    /**
     * We will return {@link End} if this value is 0,
     * which means the current fragment is a continuous frame.
     * If this value is 1, which means the current fragment
     * is the last frame, we will return the next section
     * to react to the message. Additionally, we will
     * clear the payload cache.
     */
    private final byte fin;

    /**
     * We will clear the data in this cache
     * if the FIN bit is 1.
     */
    private final ByteArrayOutputStream cache;

    /**
     * We will move to the next section using this
     * if the FIN bit is 1.
     */
    private final FrameSection origin;

    /**
     * Constructor.
     * @param fin See {@link Defragmentation#fin}.
     * @param cache See {@link Defragmentation#cache}.
     * @param origin See {@link Defragmentation#origin}.
     */
    public Defragmentation(final byte fin, final ByteArrayOutputStream cache, final FrameSection origin) {
        this.fin = fin;
        this.cache = cache;
        this.origin = origin;
    }

    @Override
    public FrameSection next() {
        final FrameSection nextSection;
        if (this.fin == 1) {
            this.cache.reset();
            nextSection = this.origin.next();
        } else {
            nextSection = new End();
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
