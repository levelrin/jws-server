/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * When the {@link Socket} object is created from the {@link ServerSocket#accept()},
 * we could not use the try-with-resources statement because we create a thread to handle the communication.
 * In other words, try-with-resources statement will close the socket
 * immediately after the {@link ServerSocket#accept()}.
 * For that reason, we need to make sure that the socket is closed when the communication ends or exception occurs.
 * It's a decorator to close the socket after the encapsulated object's job is done.
 * It will catch the generic exception, close the socket, and rethrow it.
 */
public final class CloseSocketWhenDone implements WsServer {

    /**
     * We will close this.
     */
    private final Socket socket;

    /**
     * We will close the socket when this finishes its job.
     */
    private final WsServer origin;

    /**
     * Constructor.
     * @param socket See {@link CloseSocketWhenDone#socket}.
     * @param origin See {@link CloseSocketWhenDone#origin}.
     */
    public CloseSocketWhenDone(final Socket socket, final WsServer origin) {
        this.socket = socket;
        this.origin = origin;
    }

    @Override
    @SuppressWarnings(
        {
        "IllegalCatch",
        "PMD.AvoidCatchingGenericException",
        // This is a false positive. Please see the ref #1 in the code.
        "PMD.PreserveStackTrace"
        }
    )
    public void start() {
        try {
            this.origin.start();
            this.socket.close();
        } catch (final Exception exception) {
            try {
                this.socket.close();
            } catch (final IOException ioException) {
                // ref #1
                // We added the above generic exception into the IOException.
                ioException.addSuppressed(exception);
                throw new IllegalStateException(
                    "Failed to close the socket.",
                    ioException
                );
            }
            throw new IllegalStateException(
                "We closed the socket due to an exception.",
                exception
            );
        }
    }

}
