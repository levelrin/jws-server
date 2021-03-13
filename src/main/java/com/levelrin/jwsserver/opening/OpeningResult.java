/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

/**
 * It represents the result of the WebSocket opening handshake.
 */
public interface OpeningResult {

    /**
     * True if the opening handshake succeeded.
     * Otherwise, false.
     * @return Whether the opening handshake was successful or not.
     */
    boolean success();

    /**
     * The details of the opening handshake result.
     * @return Details of the opening handshake result.
     */
    String message();

}
