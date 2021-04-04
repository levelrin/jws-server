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
 * It's responsible for responding to the client when the opening handshake failed.
 * It will reply to the client and close the socket.
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
     * Constructor.
     * @param writer See {@link InformOpeningFailure#writer}.
     * @param openingResult See {@link InformOpeningFailure#writer}.
     */
    public InformOpeningFailure(final PrintWriter writer, final OpeningResult openingResult) {
        this.writer = writer;
        this.openingResult = openingResult;
    }

    @Override
    public void start() {
        this.writer.println(this.openingResult.message());
        this.writer.flush();
        this.writer.close();
    }

}
