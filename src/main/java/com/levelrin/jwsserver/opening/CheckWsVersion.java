/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.Map;

/**
 * A decorator to check the WebSocket version.
 * The WebSocket version must be 13 to be valid.
 * Otherwise, the opening handshake will fail.
 */
public final class CheckWsVersion implements Opening {

    /**
     * HTTP headers of the request from the client.
     */
    private final Map<String, String> headers;

    /**
     * We will decorate this.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param headers See {@link CheckWsVersion#headers}.
     * @param origin See {@link CheckWsVersion#origin}.
     */
    public CheckWsVersion(final Map<String, String> headers, final Opening origin) {
        this.headers = headers;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final OpeningResult result;
        if ("13".equals(this.headers.get("SEC-WEBSOCKET-VERSION"))) {
            result = this.origin.handshake();
        } else {
            result = new OpeningResult() {

                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "The Sec-WebSocket-Version must be 13.";
                }

            };
        }
        return result;
    }

}
