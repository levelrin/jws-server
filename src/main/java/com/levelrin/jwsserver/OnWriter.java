/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * It's responsible for create a {@link PrintWriter} object
 * from the {@link java.net.Socket}.
 * We will use the {@link PrintWriter} to reply to the client.
 */
public final class OnWriter implements WsServer {

    /**
     * We will create a {@link PrintWriter} out of this.
     */
    private final Socket socket;

    /**
     * A function to handle the error from the output stream of the socket.
     */
    private final Consumer<IOException> onError;

    /**
     * We can reply to the client using this.
     */
    private final Function<PrintWriter, WsServer> withWriter;

    /**
     * Constructor.
     * @param socket See {@link OnWriter#socket}.
     * @param onError See {@link OnWriter#onError}.
     * @param withWriter See {@link OnWriter#withWriter}.
     */
    public OnWriter(final Socket socket, final Consumer<IOException> onError, final Function<PrintWriter, WsServer> withWriter) {
        this.socket = socket;
        this.onError = onError;
        this.withWriter = withWriter;
    }

    @Override
    public void start() {
        try {
            this.withWriter.apply(
                new PrintWriter(
                    this.socket.getOutputStream(),
                    false,
                    StandardCharsets.UTF_8
                )
            ).start();
        } catch (final IOException ioException) {
            this.onError.accept(ioException);
        }
    }

}
