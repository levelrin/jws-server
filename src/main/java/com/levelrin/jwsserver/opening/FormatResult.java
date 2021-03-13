/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

/**
 * A decorator to wrap the opening handshake result and form a HTTP response message.
 */
public final class FormatResult implements Opening {

    /**
     * We will encapsulate this.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param origin See {@link FormatResult#origin}.
     */
    public FormatResult(final Opening origin) {
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        return new Result(this.origin.handshake());
    }

    /**
     * Opening handshake result.
     */
    private static class Result implements OpeningResult {

        /**
         * The opening handshake result from the encapsulated object.
         */
        private final OpeningResult origin;

        /**
         * Constructor.
         * @param origin See {@link Result#origin}.
         */
        Result(final OpeningResult origin) {
            this.origin = origin;
        }

        @Override
        public boolean success() {
            return this.origin.success();
        }

        @Override
        public String message() {
            final String response;
            if (this.origin.success()) {
                response = this.origin.message();
            } else {
                response = String.format(
                    """
                    HTTP/1.1 400 Bad Request
                    Content-Type: application/json

                    {
                       "about":"openingHandshake",
                       "success":false,
                       "message":"%s"
                    }
                    """,
                    this.origin.message()
                );
            }
            return response;
        }

    }

}
