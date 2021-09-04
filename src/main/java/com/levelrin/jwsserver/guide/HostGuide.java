/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import java.util.Map;

/**
 * According to rfc6455, we are supposed to validate the HTTP request headers from the client
 * during the opening handshake.
 * One of the validation rules states "A |Host| header field containing the server's authority."
 *
 * It's responsible for checking the host header if it contains the host specified by you.
 */
public final class HostGuide {

    /**
     * We will use this key to store the host into the map.
     */
    private static final String KEY = "host";

    /**
     * We will store all the dependencies for composing objects.
     */
    private final Map<String, Object> dependencies;

    /**
     * Constructor.
     * @param dependencies See {@link HostGuide#dependencies}.
     */
    public HostGuide(final Map<String, Object> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Set the host for the handshake validation.
     * @param host We will check if the |Host| header contains this.
     * @return Next guide.
     */
    public PongGuide host(final String host) {
        this.dependencies.put(KEY, host);
        return new PongGuide(this.dependencies);
    }

    /**
     * We will check if the |Host| header contains "localhost".
     * @return Next guide.
     */
    public PongGuide localhost() {
        this.dependencies.put(KEY, "localhost");
        return new PongGuide(this.dependencies);
    }

    /**
     * You can use this if you don't want to bother checking the host.
     * We will set the host as an empty string, which effectively accepts any host.
     * @return Next guide.
     */
    @SuppressWarnings("MethodName")
    public PongGuide skipHostValidation() {
        this.dependencies.put(KEY, "");
        return new PongGuide(this.dependencies);
    }

}
