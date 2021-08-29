/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session;

import com.levelrin.jwsserver.session.component.OutgoingBody;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * The id of this session is generated by {@link java.util.UUID}.
 * It's not thread-safe.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class UuidSession implements Session {

    /**
     * We will use this to send a message to the client.
     */
    private final Socket socket;

    /**
     * We will store the ID into this.
     */
    private final List<String> idCache = new ArrayList<>(1);

    /**
     * Constructor.
     * @param socket See {@link UuidSession#socket}.
     */
    public UuidSession(final Socket socket) {
        this.socket = socket;
    }

    @Override
    @SuppressWarnings("PMD.ShortMethodName")
    public String id() {
        if (this.idCache.isEmpty()) {
            this.idCache.add(
                UUID.randomUUID().toString()
            );
        }
        return this.idCache.get(0);
    }

    /**
     * It will not add a line break at the end.
     * We assume the message is in UTF-8.
     * It always send the message in a complete frame without fragmentation.
     * @param message Content of the message.
     */
    @Override
    @SuppressWarnings("MagicNumber")
    public void sendMessage(final String message) {
        new OutgoingBody(
            this.socket,
            // -127 is equivalent to 10000001,
            // which means it's a FIN frame with TEXT data.
            -127,
            message.getBytes(StandardCharsets.UTF_8)
        ).send();
    }

    /**
     * It always send the message in a complete frame without fragmentation.
     * @param message Content of the message.
     */
    @Override
    @SuppressWarnings("MagicNumber")
    public void sendMessage(final byte[] message) {
        new OutgoingBody(
            this.socket,
            // -126 is equivalent to 10000010,
            // which means it's a FIN frame with binary data.
            -126,
            message
        ).send();
    }

    /**
     * Close the connection with the status code of 1000 and the empty reason.
     */
    @Override
    @SuppressWarnings("MagicNumber")
    public void close() {
        new OutgoingBody(
            this.socket,
            // -120 is equivalent to 10001000,
            // which means it's a FIN close frame.
            -120,
            new byte[] {
                // 1000 (status code) is equivalent to 0000001111101000.
                // 00000011 (first byte) is equivalent to 3.
                // 11101000 (second byte) is equivalent to -24
                3, -24,
            }
        ).send();
    }

    @Override
    public <T> T advanced(final Function<Socket, T> withSocket) {
        return withSocket.apply(this.socket);
    }

}
