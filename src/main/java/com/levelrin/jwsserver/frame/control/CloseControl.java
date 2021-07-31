/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.FrameSection;
import com.levelrin.jwsserver.frame.StopFraming;
import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;

/**
 * It checks if the opcode denotes a connection close.
 */
public final class CloseControl implements FrameControl {

    /**
     * We are about to end this session.
     */
    private final Session session;

    /**
     * We can do something in response to the client's closing message using this.
     */
    private final Reaction reaction;

    /**
     * Constructor.
     * @param session See {@link CloseControl#session}.
     * @param reaction See {@link CloseControl#reaction}.
     */
    public CloseControl(final Session session, final Reaction reaction) {
        this.session = session;
        this.reaction = reaction;
    }

    @Override
    public boolean match(final String opcode) {
        return "1000".equals(opcode);
    }

    @Override
    public FrameSection section() {
        this.reaction.onClose(this.session);
        return new StopFraming();
    }

}
