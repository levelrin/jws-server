/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.End;
import com.levelrin.jwsserver.frame.FrameSection;
import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;

/**
 * It checks if the opcode denotes a binary frame.
 */
public final class BinaryControl implements FrameControl {

    /**
     * This is the unmasked payload data from the client.
     */
    private final byte[] unmaskedPayload;

    /**
     * We can send messages to the client using this.
     */
    private final Session session;

    /**
     * We can do something in response to the client's binary message using this.
     */
    private final Reaction reaction;

    /**
     * Constructor.
     * @param unmaskedPayload See {@link BinaryControl#unmaskedPayload}.
     * @param session See {@link BinaryControl#session}.
     * @param reaction See {@link BinaryControl#reaction}.
     */
    public BinaryControl(final byte[] unmaskedPayload, final Session session, final Reaction reaction) {
        this.unmaskedPayload = unmaskedPayload.clone();
        this.session = session;
        this.reaction = reaction;
    }

    @Override
    public boolean match(final String opcode) {
        return "0010".equals(opcode);
    }

    @Override
    public FrameSection section() {
        this.reaction.onMessage(this.session, this.unmaskedPayload.clone());
        return new End();
    }

}
