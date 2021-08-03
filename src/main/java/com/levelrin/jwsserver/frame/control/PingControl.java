/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.End;
import com.levelrin.jwsserver.frame.FrameSection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * It checks if the opcode denotes a ping.
 * If so, it will send a pong frame back.
 */
public final class PingControl implements FrameControl {

    /**
     * It's for sending a pong frame back.
     */
    private final Socket socket;

    /**
     * Constructor.
     * @param socket See {@link PingControl#socket}.
     */
    public PingControl(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public boolean match(final String opcode) {
        return "1001".equals(opcode);
    }

    @Override
    @SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
    public FrameSection section() {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // -118 is equivalent to 10001010,
            // which means it's a FIN pong frame.
            stream.write(-118);
            // 0 is equivalent to 00000000,
            // which means the payload data is not masked and length is 0.
            stream.write(0);
            final OutputStream output = this.socket.getOutputStream();
            output.write(stream.toByteArray());
            output.flush();
        } catch (final IOException ioException) {
            throw new IllegalStateException(
                "Failed to send a pong frame back.",
                ioException
            );
        }
        return new End();
    }

}
