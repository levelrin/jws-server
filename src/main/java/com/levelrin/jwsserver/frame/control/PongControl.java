/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.End;
import com.levelrin.jwsserver.frame.FrameSection;
import com.levelrin.jwsserver.session.Session;
import java.util.function.Consumer;

/**
 * It checks if the opcode denotes a pong.
 */
public final class PongControl implements FrameControl {

    /**
     * The user will use this to check which communication
     * the pong frame belongs to.
     */
    private final Session session;

    /**
     * A callback function when the server receives a pong frame.
     */
    private final Consumer<Session> onPong;

    /**
     * Constructor.
     * @param session See {@link PongControl#session}.
     * @param onPong See {@link PongControl#onPong}.
     */
    public PongControl(final Session session, final Consumer<Session> onPong) {
        this.session = session;
        this.onPong = onPong;
    }

    @Override
    public boolean match(final String opcode) {
        return "1010".equals(opcode);
    }

    @Override
    public FrameSection section() {
        this.onPong.accept(this.session);
        return new End();
    }

}
