/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

/**
 * It represents the WebSocket opening handshake process.
 */
public interface Opening {

    /**
     * Do the opening handshake.
     * @return The result of the opening handshake.
     */
    OpeningResult handshake();

}
