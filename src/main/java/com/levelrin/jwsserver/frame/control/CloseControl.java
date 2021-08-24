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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * It checks if the opcode denotes a connection close.
 */
public final class CloseControl implements FrameControl {

    /**
     * This contains the status code and reason.
     */
    private final byte[] unmaskedPayload;

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
     * @param unmaskedPayload See {@link CloseControl#unmaskedPayload}.
     * @param session See {@link CloseControl#session}.
     * @param reaction See {@link CloseControl#reaction}.
     */
    public CloseControl(final byte[] unmaskedPayload, final Session session, final Reaction reaction) {
        this.unmaskedPayload = unmaskedPayload.clone();
        this.session = session;
        this.reaction = reaction;
    }

    @Override
    public boolean match(final String opcode) {
        return "1000".equals(opcode);
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public FrameSection section() {
        // We are converting the binary number into decimal.
        final int code = Integer.parseInt(
            // We are concatenating the first two bytes in binary form.
            // For example, let's say the first byte is 00000011.
            // And the second byte is 11101000.
            // The result would be 0000001111101000.
            String.format(
                "%8s%8s",
                // We are converting the bytes into unsigned bytes.
                Integer.toBinaryString(this.unmaskedPayload[0] & 0xff),
                Integer.toBinaryString(this.unmaskedPayload[1] & 0xff)
            ).replace(' ', '0'),
            2
        );
        final String reason = new String(
            Arrays.copyOfRange(
                this.unmaskedPayload,
                2,
                this.unmaskedPayload.length
            ),
            StandardCharsets.UTF_8
        );
        this.reaction.onClose(this.session, code, reason);
        return new StopFraming();
    }

}
