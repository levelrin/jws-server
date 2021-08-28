/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.advanced;

import com.levelrin.jwsserver.session.component.OutgoingBody;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * It offers more advanced way to close the WebSocket communication.
 */
public final class AdvancedClose {

    /**
     * It's for sending a closing frame to the client.
     */
    private final Socket socket;

    /**
     * Constructor.
     * @param socket See {@link AdvancedClose#socket}.
     */
    public AdvancedClose(final Socket socket) {
        this.socket = socket;
    }

    /**
     * Send a closing frame to the client with the specified status code and reason.
     * @param code Status code.
     * @param reason Reason to close the communication.
     */
    @SuppressWarnings("MagicNumber")
    public void close(final short code, final String reason) {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(
                ByteBuffer.allocate(2).putShort(code).array()
            );
            stream.write(reason.getBytes(StandardCharsets.UTF_8));
            new OutgoingBody(
                this.socket,
                // -120 is equivalent to 10001000,
                // which means it's a FIN close frame.
                -120,
                stream.toByteArray()
            ).send();
        } catch (final IOException ioException) {
            throw new IllegalStateException("Failed to send a closing frame.", ioException);
        }
    }

}
