/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * When the server socket receives a connection,
 * meaning that we got a new socket,
 * we will handle the socket communication in a new thread to listen to other clients in parallel.
 *
 * It's responsible for setting the {@link java.util.concurrent.ExecutorService}.
 * We will use that to create a new thread.
 */
public final class SocketThreadGuide {

    /**
     * We will store the {@link ExecutorService} into the map using this key.
     */
    private static final String KEY = "socketThread";

    /**
     * We will store all the dependencies for composing objects.
     */
    private final Map<String, Object> dependencies;

    /**
     * Constructor.
     * @param dependencies See {@link SocketThreadGuide#dependencies}.
     */
    public SocketThreadGuide(final Map<String, Object> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Set the executor service for the socket communication.
     * @param executorService We will create a new thread using this.
     * @return Next guide.
     */
    public HostGuide socketThread(final ExecutorService executorService) {
        this.dependencies.put(KEY, executorService);
        return new HostGuide(this.dependencies);
    }

    /**
     * We will use the same executor service that you set for the server thread.
     * @return Next guide.
     */
    @SuppressWarnings("MethodName")
    public HostGuide defaultSocketThread() {
        this.dependencies.put(
            KEY,
            this.dependencies.get("serverThread")
        );
        return new HostGuide(this.dependencies);
    }

}
