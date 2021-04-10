/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;

/**
 * A decorator to call {@link Reaction#onStart(Session)}.
 * After the call, we will start the encapsulated object.
 */
public final class StartCommunication implements WsServer {

    /**
     * The user can do something on start using this.
     */
    private final Reaction reaction;

    /**
     * The user may use session to a send message to the client on start.
     */
    private final Session session;

    /**
     * We will start this after calling the user's logic on the communication start.
     */
    private final WsServer origin;

    /**
     * Constructor.
     * @param reaction See {@link StartCommunication#reaction}.
     * @param session See {@link StartCommunication#session}.
     * @param origin See {@link StartCommunication#origin}.
     */
    public StartCommunication(final Reaction reaction, final Session session, final WsServer origin) {
        this.reaction = reaction;
        this.session = session;
        this.origin = origin;
    }

    @Override
    public void start() {
        this.reaction.onStart(this.session);
        this.origin.start();
    }

}
