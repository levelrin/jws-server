/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.List;

/**
 * A decorator to check the HTTP version of the client's request.
 * The version must be 1.1 or higher.
 * Otherwise, the opening handshake will fail.
 */
public final class CheckHttpVersion implements Opening {

    /**
     * Request lines from the client.
     */
    private final List<String> lines;

    /**
     * We will run this if it passes the validation.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param lines See {@link CheckHttpVersion#lines}.
     * @param origin See {@link CheckHttpVersion#origin}.
     */
    public CheckHttpVersion(final List<String> lines, final Opening origin) {
        this.lines = lines;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final String firstLine = this.lines.get(0);
        final double httpVersion = Double.parseDouble(
            firstLine.substring(
                firstLine.lastIndexOf("HTTP/") + "HTTP/".length()
            )
        );
        final OpeningResult result;
        final double minVersion = 1.1;
        if (httpVersion < minVersion) {
            result = new OpeningResult() {

                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "HTTP version must be 1.1 or higher.";
                }

            };
        } else {
            result = this.origin.handshake();
        }
        return result;
    }

}
