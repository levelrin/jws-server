/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import java.util.concurrent.ExecutorService;

/**
 * A decorator of {@link WsServer}.
 * It creates a thread and run the encapsulated object.
 *
 * Motivation:
 * Once we receive a new connection from a client, we want to handle the communication in a separate thread
 * to receive more connections.
 * In other words, it allows us to accept multiple clients.
 */
public final class WithThread implements WsServer {

    /**
     * We will use this to create a new thread.
     */
    private final ExecutorService executorService;

    /**
     * It's the encapsulated object.
     */
    private final WsServer origin;

    /**
     * Constructor.
     * @param executorService See {@link WithThread#executorService}.
     * @param origin See {@link WithThread#origin}.
     */
    public WithThread(final ExecutorService executorService, final WsServer origin) {
        this.executorService = executorService;
        this.origin = origin;
    }

    @Override
    public void start() {
        this.executorService.execute(this.origin::start);
    }

}
