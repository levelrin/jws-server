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
 * We can pass a function to do something with the opening handshake result.
 */
public final class OnOpeningResult implements WsServer {

    /**
     * It's responsible for the opening handshake.
     */
    private final Opening opening;

    /**
     * We can do something with the handshake result using this.
     */
    private final Function<OpeningResult, WsServer> onResult;

    /**
     * Constructor.
     * @param opening See {@link OnOpeningResult#opening}.
     * @param onResult See {@link OnOpeningResult#onResult}.
     */
    public OnOpeningResult(final Opening opening, final Function<OpeningResult, WsServer> onResult) {
        this.opening = opening;
        this.onResult = onResult;
    }

    @Override
    public void start() {
        this.onResult.apply(
            this.opening.handshake()
        ).start();
    }

}
