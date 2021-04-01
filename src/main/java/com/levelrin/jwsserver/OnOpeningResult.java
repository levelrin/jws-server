/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.opening.Opening;
import com.levelrin.jwsserver.opening.OpeningResult;
import java.util.function.Function;

/**
 * It's responsible for running the {@link Opening#handshake()},
 * which generates {@link OpeningResult}.
 * We can pass two functions to do something with the opening handshake result.
 * One for the successful handshake, another for the failed handshake.
 */
public final class OnOpeningResult implements WsServer {

    /**
     * It's responsible for the opening handshake.
     */
    private final Opening opening;

    /**
     * A function that we will use when the opening handshake was successful.
     */
    private final Function<OpeningResult, WsServer> onSuccess;

    /**
     * A function that we will use when the opening handshake failed.
     */
    private final Function<OpeningResult, WsServer> onFailure;

    /**
     * Constructor.
     * @param opening See {@link OnOpeningResult#opening}.
     * @param onSuccess See {@link OnOpeningResult#onSuccess}.
     * @param onFailure See {@link OnOpeningResult#onFailure}.
     */
    public OnOpeningResult(final Opening opening, final Function<OpeningResult, WsServer> onSuccess, final Function<OpeningResult, WsServer> onFailure) {
        this.opening = opening;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    @Override
    public void start() {
        final OpeningResult result = this.opening.handshake();
        if (result.success()) {
            this.onSuccess.apply(result);
        } else {
            this.onFailure.apply(result);
        }
    }

}
