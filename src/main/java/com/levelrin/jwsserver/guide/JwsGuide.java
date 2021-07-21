/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is not essential.
 * To start a WebSocket server, you are supposed to create a {@link com.levelrin.jwsserver.WsServer} object.
 * However, you will need to compose overwhelming number of objects via the decorator pattern to do so.
 * Although we recommend you to learn how to compose them for full control, we understand that it might be
 * difficult if you are new to this library.
 * For that reason, we provide a convenient way to compose objects for a quick start.
 * You can just instantiate this class and go with the flow by checking its methods :)
 */
public final class JwsGuide {

    /**
     * We will store the {@link ExecutorService} into the map using this key.
     */
    private static final String KEY = "serverThread";

    /**
     * We will store all the dependencies for composing objects.
     */
    private final Map<String, Object> dependencies;

    /**
     * Secondary constructor.
     * We will use a simple hash map.
     */
    public JwsGuide() {
        this(new HashMap<>());
    }

    /**
     * Primary constructor.
     * @param dependencies See {@link JwsGuide#dependencies}.
     */
    public JwsGuide(final Map<String, Object> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Set the executor service for the server.
     * @param executorService We will create a thread and run the server.
     * @return Next guide.
     */
    public ServerSocketGuide serverThread(final ExecutorService executorService) {
        this.dependencies.put(KEY, executorService);
        return new ServerSocketGuide(this.dependencies);
    }

    /**
     * We will use {@link Executors#newCachedThreadPool()} to run the server.
     * @return Next guide.
     */
    @SuppressWarnings("MethodName")
    public ServerSocketGuide defaultServerThread() {
        this.dependencies.put(KEY, Executors.newCachedThreadPool());
        return new ServerSocketGuide(this.dependencies);
    }

}
