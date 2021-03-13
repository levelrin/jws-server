/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * It's responsible for parsing the HTTP headers from the request lines.
 * The keys and values of the header will be all capitalized for the case-insensitive validations later.
 */
public final class OnHeaders implements Opening {

    /**
     * HTTP request lines from the client.
     */
    private final List<String> lines;

    /**
     * You can do something with the HTTP headers with this.
     * The map represents the HTTP headers.
     * The keys and values of the header will be all capitalized
     * for the case-insensitive validations later.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
    private final Function<Map<String, String>, Opening> onHeaders;

    /**
     * Constructor.
     * @param lines See {@link OnHeaders#lines}.
     * @param onHeaders See {@link OnHeaders#onHeaders}.
     */
    public OnHeaders(final List<String> lines, final Function<Map<String, String>, Opening> onHeaders) {
        this.lines = lines;
        this.onHeaders = onHeaders;
    }

    @Override
    public OpeningResult handshake() {
        final Map<String, String> headers = new HashMap<>();
        for (final String line : this.lines.subList(1, this.lines.size())) {
            if (line.isEmpty()) {
                break;
            }
            final String[] pair = line.split(":", 2);
            headers.put(
                pair[0].toUpperCase(Locale.ROOT).trim(),
                pair[1].toUpperCase(Locale.ROOT).trim()
            );
        }
        return this.onHeaders.apply(headers).handshake();
    }

}
