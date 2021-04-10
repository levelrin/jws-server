/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.List;

/**
 * A decorator to check the request endpoint from the client.
 * The opening result will fail if the request endpoint is unknown.
 * We assume endpoints are case-sensitive.
 */
public final class CheckEndpoint implements Opening {

    /**
     * We will find the request endpoint from this.
     * We expect you to generate the request lines
     * from {@link com.levelrin.jwsserver.OnRequestLines}.
     */
    private final List<String> requestLines;

    /**
     * This is the list of known endpoints.
     * We will check if the request endpoint is known.
     * We assume all the endpoints start with slash (/).
     */
    private final List<String> endpoints;

    /**
     * We will execute this if the endpoint was valid.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param requestLines See {@link CheckEndpoint#requestLines}.
     * @param endpoints See {@link CheckEndpoint#endpoints}.
     * @param origin See {@link CheckEndpoint#origin}.
     */
    public CheckEndpoint(final List<String> requestLines, final List<String> endpoints, final Opening origin) {
        this.requestLines = requestLines;
        this.endpoints = endpoints;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final String firstLine = this.requestLines.get(0);
        final String endpoint = firstLine.split(" ")[1];
        final OpeningResult result;
        if (this.endpoints.contains(endpoint)) {
            result = this.origin.handshake();
        } else {
            result = new Fail(endpoint);
        }
        return result;
    }

    /**
     * Opening handshake failure due to invalid request endpoint.
     */
    private static final class Fail implements OpeningResult {

        /**
         * The request endpoint from the client.
         */
        private final String requestEndpoint;

        /**
         * Constructor.
         * @param requestEndpoint See {@link Fail#requestEndpoint}.
         */
        Fail(final String requestEndpoint) {
            this.requestEndpoint = requestEndpoint;
        }

        @Override
        public boolean success() {
            return false;
        }

        @Override
        public String message() {
            return String.format(
                "The request endpoint '%s' is unknown.",
                this.requestEndpoint
            );
        }

    }

}
