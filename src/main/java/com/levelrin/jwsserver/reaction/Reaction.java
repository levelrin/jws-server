/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.reaction;

import com.levelrin.jwsserver.session.Session;

/**
 * It's responsible for reacting to the message from the client.
 * We could have used the term 'response.'
 * However, we decided to call it 'reaction'
 * because many other HTTP libraries are already using the term 'response,' and we wanted to avoid confusion.
 *
 * This framework's user should create a class that implements this interface
 * to define their logic to do something when the server receives a message from the client.
 */
public interface Reaction {

    /**
     * This defines an endpoint of the server.
     * The logic defined in this object will be executed
     * only if a client's message is pointing to this endpoint.
     * The endpoint must start with slash at the beginning.
     * For example, let's say a client sends a message to 'ws://localhost/yoi'.
     * The endpoint must be '/yoi' to respond to that message.
     * @return A server endpoint in which this object is responding to.
     */
    String endpoint();

    /**
     * Do something when the WebSocket communication starts.
     * For example, you can use the {@param session} to send a message to the client.
     * Or, you can store the {@param session} and use it to send a message to the client later.
     * @param session A new connection object.
     */
    void onStart(Session session);

    /**
     * Do something when the client sends a text message.
     * @param session A connection object.
     * @param message The message from the client.
     */
    void onMessage(Session session, String message);

    /**
     * Do something when the client sends a binary message.
     * @param session A connection object.
     * @param message The message from the client.
     */
    void onMessage(Session session, byte[] message);

    /**
     * Do something when the client sends a closing message.
     * @param session A connection object.
     */
    void onClose(Session session);

}
