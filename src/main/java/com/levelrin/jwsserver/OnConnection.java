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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * It's responsible for accepting server socket connections from clients.
 * You can do something with the connected socket by passing a function.
 */
public final class OnConnection implements WsServer {

    /**
     * It's for accepting the socket connections.
     */
    private final ServerSocket serverSocket;

    /**
     * It's for handling an exception from the server socket.
     */
    private final Consumer<IOException> onError;

    /**
     * A function to do something when the server accepts a connection.
     */
    private final Function<Socket, WsServer> onAccept;

    /**
     * Constructor.
     * @param serverSocket See {@link OnConnection#serverSocket}.
     * @param onError See {@link OnConnection#onError}.
     * @param onAccept See {@link OnConnection#onAccept}.
     */
    public OnConnection(final ServerSocket serverSocket, final Consumer<IOException> onError, final Function<Socket, WsServer> onAccept) {
        this.serverSocket = serverSocket;
        this.onError = onError;
        this.onAccept = onAccept;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public void start() {
        try {
            final Socket socket = this.serverSocket.accept();
            this.onAccept.apply(socket).start();
        } catch (final IOException ex) {
            this.onError.accept(ex);
        }
    }

}
