/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.reaction.Reaction;
import java.util.List;
import java.util.function.Function;

/**
 * It's responsible for finding the {@link Reaction} that belongs to the endpoint.
 * We assume endpoints are case-sensitive.
 * You can do something with the {@link Reaction} by passing a function to this.
 */
public final class OnReaction implements WsServer {

    /**
     * The HTTP request lines from the client.
     * We will get the endpoint from this.
     */
    private final List<String> requestLines;

    /**
     * We will find the appropriate reaction from this list.
     */
    private final List<Reaction> reactions;

    /**
     * You can do something with the reaction using this function.
     */
    private final Function<Reaction, WsServer> withReaction;

    /**
     * Constructor.
     * @param requestLines See {@link OnReaction#requestLines}.
     * @param reactions See {@link OnReaction#reactions}.
     * @param withReaction See {@link OnReaction#withReaction}.
     */
    public OnReaction(final List<String> requestLines, final List<Reaction> reactions, final Function<Reaction, WsServer> withReaction) {
        this.requestLines = requestLines;
        this.reactions = reactions;
        this.withReaction = withReaction;
    }

    @Override
    public void start() {
        final String firstLine = this.requestLines.get(0);
        final String endpoint = firstLine.split(" ")[1];
        for (final Reaction reaction : this.reactions) {
            if (reaction.endpoint().equals(endpoint)) {
                this.withReaction.apply(reaction).start();
                break;
            }
        }
    }

}
