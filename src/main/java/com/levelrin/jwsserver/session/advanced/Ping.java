/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.advanced;

import com.levelrin.jwsserver.session.component.OutgoingBody;
import java.net.Socket;

/**
 * It offers a way to ping the client.
 */
public final class Ping {

    /**
     * We will use this to send a ping frame to the client.
     */
    private final Socket socket;

    /**
     * Constructor.
     * @param socket See {@link Ping#socket}.
     */
    public Ping(final Socket socket) {
        this.socket = socket;
    }

    /**
     * Send a ping frame to the client without an application data.
     */
    public void ping() {
        // -119 is equivalent to 10001001,
        // which means it's a FIN ping frame.
        final int initialByte = -119;
        new OutgoingBody(
            this.socket,
            initialByte,
            new byte[0]
        ).send();
    }

    /**
     * Send a ping frame to the client with an application data.
     * @param data We will send this data to the client.
     */
    public void ping(final byte[] data) {
        // -119 is equivalent to 10001001,
        // which means it's a FIN ping frame.
        final int initialByte = -119;
        new OutgoingBody(
            this.socket,
            initialByte,
            data.clone()
        ).send();
    }

}
