/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.Map;

/**
 * A decorator to check the upgrade header of HTTP request from the client.
 * The upgrade value must be websocket.
 * Otherwise, the opening handshake will fail.
 */
public final class CheckUpgrade implements Opening {

    /**
     * HTTP request headers from the client.
     */
    private final Map<String, String> headers;

    /**
     * We will use this if it passes the validation.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param headers See {@link CheckUpgrade#headers}.
     * @param origin See {@link CheckUpgrade#origin}.
     */
    public CheckUpgrade(final Map<String, String> headers, final Opening origin) {
        this.headers = headers;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final OpeningResult result;
        if ("WEBSOCKET".equals(this.headers.get("UPGRADE"))) {
            result = this.origin.handshake();
        } else {
            result = new OpeningResult() {

                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "The |Upgrade| header must be 'websocket'";
                }

            };
        }
        return result;
    }

}
