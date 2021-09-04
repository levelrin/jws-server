/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import com.levelrin.jwsserver.session.Session;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

/**
 * You can do something when the server receives a pong frame using this.
 */
public final class PongGuide {

    /**
     * We will use this key to store the function into the map.
     * We will run that function when the server receives a pong frame.
     */
    private static final String KEY = "onPong";

    /**
     * We will store all the dependencies for composing objects.
     */
    private final Map<String, Object> dependencies;

    /**
     * Constructor.
     * @param dependencies See {@link PongGuide#dependencies}.
     */
    public PongGuide(final Map<String, Object> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Use this method when you don't care about the ping/pong.
     * @return Next guide.
     */
    public ReactionGuide ignorePong() {
        this.dependencies.put(
            KEY,
            (Consumer<Session>) session -> {
                // Do nothing.
            }
        );
        return new ReactionGuide(this.dependencies, new ArrayList<>());
    }

    /**
     * You can do something when the server receives a pong frame.
     * @param withSession We will execute this function.
     *                    The parameter is the session that the pong frame belongs.
     * @return Next guide.
     */
    public ReactionGuide onPong(final Consumer<Session> withSession) {
        this.dependencies.put(KEY, withSession);
        return new ReactionGuide(this.dependencies, new ArrayList<>());
    }

}
