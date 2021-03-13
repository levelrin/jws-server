/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import java.net.ServerSocket;

/**
 * A decorator to constantly execute the encapsulated object as long as the {@link ServerSocket} is open.
 *
 * Motivation:
 * We want to continuously accept requests from clients.
 */
public final class WithLoop implements WsServer {

    /**
     * We want to constantly accept requests from client until this serve socket is closed.
     */
    private final ServerSocket serverSocket;

    /**
     * We will encapsulate this.
     */
    private final WsServer origin;

    /**
     * Constructor.
     * @param serverSocket See {@link WithLoop#serverSocket}.
     * @param origin See {@link WithLoop#origin}.
     */
    public WithLoop(final ServerSocket serverSocket, final WsServer origin) {
        this.serverSocket = serverSocket;
        this.origin = origin;
    }

    @Override
    public void start() {
        while (!this.serverSocket.isClosed()) {
            this.origin.start();
        }
    }

}
