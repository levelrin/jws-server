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
 * Since it's a decorator, we can decorate an object to take further actions after responding.
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
     * We will use this after replying the message to the client.
     */
    private final WsServer origin;

    /**
     * Constructor.
     * @param writer See {@link InformOpeningSuccess#writer}.
     * @param result See {@link InformOpeningSuccess#result}.
     * @param origin See {@link InformOpeningSuccess#origin}.
     */
    public InformOpeningSuccess(final PrintWriter writer, final OpeningResult result, final WsServer origin) {
        this.writer = writer;
        this.result = result;
        this.origin = origin;
    }

    @Override
    public void start() {
        this.writer.println(
            this.result.message()
        );
        this.writer.flush();
        this.origin.start();
    }

}
