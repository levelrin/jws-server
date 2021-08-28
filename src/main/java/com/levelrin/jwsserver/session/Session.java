/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session;

import java.net.Socket;
import java.util.function.Function;

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

    /**
     * Use this when the basic methods are not good enough for you.
     * Please see the options in the {@link com.levelrin.jwsserver.session.option} package.
     * To select an option, you can instantiate an object and pass it as a parameter of this method.
     * You will see more advanced methods from the returned object.
     * @param withSocket It's for creating a new object with more advanced features.
     * @param <T> The type of object that offers more features.
     * @return An object with more features created by {@code withSocket}.
     */
    <T> T advanced(Function<Socket, T> withSocket);

}
