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
 * A decorator to respond to the client when the opening handshake failed.
 * It will reply to the client and close the socket.
 * If the opening handshake was successful, it will run the encapsulated object.
 *
 * todo: remove the condition because OnOpeningResult is checking already.
 */
public final class InformOpeningFailure implements WsServer {

    /**
     * We will reply to the client using this.
     */
    private final PrintWriter writer;

    /**
     * The result of the opening handshake.
     */
    private final OpeningResult openingResult;

    /**
     * We will use this if the opening handshake was successful.
     */
    private final WsServer origin;

    /**
     * Constructor.
     * @param writer See {@link InformOpeningFailure#writer}.
     * @param openingResult See {@link InformOpeningFailure#writer}.
     * @param origin See {@link InformOpeningFailure#origin}.
     */
    public InformOpeningFailure(final PrintWriter writer, final OpeningResult openingResult, final WsServer origin) {
        this.writer = writer;
        this.openingResult = openingResult;
        this.origin = origin;
    }

    @Override
    public void start() {
        if (this.openingResult.success()) {
            this.origin.start();
        } else {
            this.writer.println(this.openingResult.message());
            this.writer.flush();
            this.writer.close();
        }
    }

}
