/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame;

/**
 * It represents a section in a WebSocket frame.
 * For example, it can be a FIN, RSV, opcode, MASK, and etc.
 * It's responsible for interpreting the binary data from the socket.
 * After the interpretation, it moves to the next section.
 * The next section can be a normal next section, self again, initial section, or
 * some artificially created section by us to perform some action.
 */
public interface FrameSection {

    /**
     * Moves to the next section.
     * @return Next section.
     */
    FrameSection next();

    /**
     * It tells us whether we should continue to call next or not.
     * True if we should continue. False otherwise.
     * @return Whether we should continue to call next or not.
     */
    boolean shouldContinue();

    /**
     * It checks if this section is the end of a frame.
     * If it's the end of a frame, we should interpret the next data from FIN section again.
     * @return True if it's the end of a frame. Otherwise, false.
     */
    boolean isEnd();

}
