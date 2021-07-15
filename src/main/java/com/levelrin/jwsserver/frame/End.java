/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

/**
 * It represents the end of of the frame section.
 */
public final class End implements FrameSection {

    @Override
    public FrameSection next() {
        throw new UnsupportedOperationException(
            "This is the end of the frame."
        );
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public boolean isEnd() {
        return true;
    }

}
