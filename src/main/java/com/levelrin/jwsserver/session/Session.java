/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session;

/**
 * It represents a unique connection between the server and the client.
 * It's responsible for sending a message to the specific client.
 * We can also use this object to close the connection from the server.
 */
public interface Session {

    /**
     * A unique identifier of the connection.
     * We can use this to find the specific client.
     * @return Unique identifier.
     */
    @SuppressWarnings("PMD.ShortMethodName")
    String id();

    /**
     * Send a text message to the client.
     * @param message Content of the message.
     */
    void sendMessage(String message);

    /**
     * Send a binary message to the client.
     * @param message Content of the message.
     */
    void sendMessage(byte[] message);

    /**
     * Close the connection.
     */
    void close();

}
