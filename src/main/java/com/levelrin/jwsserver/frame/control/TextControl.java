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
import java.nio.charset.StandardCharsets;

/**
 * It checks if the opcode denotes a text frame.
 */
public final class TextControl implements FrameControl {

    /**
     * This is the text from the client in bytes.
     */
    private final byte[] unmaskedPayload;

    /**
     * We can send messages to the client using this.
     */
    private final Session session;

    /**
     * We can do something in response to the client's text message using this.
     */
    private final Reaction reaction;

    /**
     * Constructor.
     * @param unmaskedPayload See {@link TextControl#unmaskedPayload}.
     * @param session See {@link TextControl#session}.
     * @param reaction See {@link TextControl#reaction}.
     */
    public TextControl(final byte[] unmaskedPayload, final Session session, final Reaction reaction) {
        this.unmaskedPayload = unmaskedPayload.clone();
        this.session = session;
        this.reaction = reaction;
    }

    @Override
    public boolean match(final String opcode) {
        return "0001".equals(opcode);
    }

    @Override
    public FrameSection section() {
        final String text = new String(this.unmaskedPayload, StandardCharsets.UTF_8);
        this.reaction.onMessage(this.session, text);
        return new End();
    }

}
