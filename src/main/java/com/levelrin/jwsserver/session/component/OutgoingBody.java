/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * It represents the payload data that the server wants to send.
 */
public final class OutgoingBody {

    /**
     * We will send the payload using this.
     */
    private final Socket socket;

    /**
     * Initial byte of the frame in decimal.
     * In other words, it contains FIN, RSV1, RSV2, RSV3, and opcode.
     * For example, if you want to send a FIN frame with TEXT data,
     * you should pass -127, which is equivalent to 10000001.
     */
    private final int initialByte;

    /**
     * The payload data that we are going to send.
     */
    private final byte[] payload;

    /**
     * Constructor.
     * @param socket See {@link OutgoingBody#socket}.
     * @param initialByte See {@link OutgoingBody#initialByte}.
     * @param payload See {@link OutgoingBody#payload}.
     */
    public OutgoingBody(final Socket socket, final int initialByte, final byte[] payload) {
        this.socket = socket;
        this.initialByte = initialByte;
        this.payload = payload.clone();
    }

    /**
     * Send the message to the client.
     */
    @SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
    public void send() {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(this.initialByte);
            final int length = this.payload.length;
            // If the content length is less than 126,
            // we don't have to worry about the extended payload length.
            if (length < 126) {
                stream.write(length);
                // If the content length is greater than 126,
                // we need to specify the extended payload length.
                // The maximum number that 16 bits can represent is 65,535.
                // So, this condition would be the extended payload length
                // with the following 2 bytes.
            } else if (length <= 65_535) {
                // 126 means the following 2 bytes would be the payload length.
                stream.write(126);
                // It's important to allocate 2 bytes
                // because the next 2 bytes would be the payload length.
                // Also, we must put short data because the short data type is 2 bytes.
                stream.write(ByteBuffer.allocate(2).putShort((short) length).array());
            } else {
                // 127 means the following 8 bytes would be the payload length.
                stream.write(127);
                // It's important to allocate 8 bytes
                // because the next 8 bytes would be the payload length.
                // Also, we must put long data because the long data type is 8 bytes.
                stream.write(ByteBuffer.allocate(8).putLong(length).array());
            }
            stream.write(this.payload);
            final OutputStream output = this.socket.getOutputStream();
            output.write(stream.toByteArray());
            output.flush();
        } catch (final IOException ioException) {
            throw new IllegalStateException(
                "Failed to send a message to the client.",
                ioException
            );
        }
    }

}
