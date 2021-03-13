/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

/**
 * It stands for WebSocket Server.
 * It's responsible for starting the server.
 */
public interface WsServer {

    /**
     * Open the socket and start to accept requests from clients.
     */
    void start();

}
