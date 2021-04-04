/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.opening.OpeningResult;
import java.io.PrintWriter;

/**
 * It's responsible for responding to the client to switch the protocol from HTTP to WebSocket.
 * It assumes that the opening handshake was successful.
 * todo: implement websocket communication logic after the successful opening handshake.
 */
public final class InformOpeningSuccess implements WsServer {

    /**
     * We will use this to reply to the client.
     */
    private final PrintWriter writer;

    /**
     * It contains the reply message to the client.
     */
    private final OpeningResult result;

    /**
     * Constructor.
     * @param writer See {@link InformOpeningSuccess#writer}.
     * @param result See {@link InformOpeningSuccess#result}.
     */
    public InformOpeningSuccess(final PrintWriter writer, final OpeningResult result) {
        this.writer = writer;
        this.result = result;
    }

    @Override
    public void start() {
        this.writer.println(
            this.result.message()
        );
        this.writer.flush();
    }

}
