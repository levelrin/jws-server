/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

/**
 * We need a {@link java.net.ServerSocket} to listen to clients.
 * It's responsible for setting the server socket.
 */
public final class ServerSocketGuide {

    /**
     * We will use this key to store {@link ServerSocket} into the map.
     */
    private static final String KEY = "serverSocket";

    /**
     * We will store all the dependencies for composing objects.
     */
    private final Map<String, Object> dependencies;

    /**
     * Constructor.
     * @param dependencies See {@link ServerSocketGuide#dependencies}.
     */
    public ServerSocketGuide(final Map<String, Object> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Set the server socket.
     * @param serverSocket We will use this to listen to clients.
     * @return Next guide.
     */
    public SocketThreadGuide serverSocket(final ServerSocket serverSocket) {
        this.dependencies.put(KEY, serverSocket);
        return new SocketThreadGuide(this.dependencies);
    }

    /**
     * We will instantiate the {@link ServerSocket} and bind it to the specified port.
     * @param port We will listen to this port.
     * @return Next guide.
     */
    public SocketThreadGuide port(final int port) {
        try {
            this.dependencies.put(KEY, new ServerSocket(port));
        } catch (final IOException ex) {
            throw new IllegalStateException("Failed to instantiate the server socket.", ex);
        }
        return new SocketThreadGuide(this.dependencies);
    }

    /**
     * We will use the port 42069 to listen to the clients.
     * @return Next guide.
     */
    public SocketThreadGuide defaultPort() {
        final int port = 42_069;
        try {
            this.dependencies.put(KEY, new ServerSocket(port));
        } catch (final IOException ex) {
            throw new IllegalStateException("Failed to instantiate the server socket.", ex);
        }
        return new SocketThreadGuide(this.dependencies);
    }

}
