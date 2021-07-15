/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

/**
 * It's a null object.
 * We can use this instead of null when we need to initialize {@link FrameSection} variable first
 * and figure out the actual value later.
 * If something went wrong (like programming mistakes) and this object is actually used to call next,
 * this will throw an exception.
 * Obviously, it will tell us that we should stop calling next.
 */
public final class StopFraming implements FrameSection {

    @Override
    @SuppressWarnings("RegexpSingleline")
    public FrameSection next() {
        throw new IllegalStateException(
            """
            It's probably a programming mistake if you see this error.
            Maybe your logic failed to initialize the FrameSection object properly.
            By the way, you can use the shouldContinue() method to make sure
            nothing went wrong before calling next().
            """
        );
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public boolean isEnd() {
        return true;
    }

}
