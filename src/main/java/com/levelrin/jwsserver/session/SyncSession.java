/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session;

/**
 * A decorator to make a {@link Session} object thread-safe.
 */
public final class SyncSession implements Session {

    /**
     * We will make this object thread-safe.
     */
    private final Session origin;

    /**
     * It's a thread lock object.
     */
    private final Object lock = new Object();

    /**
     * Constructor.
     * @param origin See {@link SyncSession#origin}.
     */
    public SyncSession(final Session origin) {
        this.origin = origin;
    }

    @Override
    @SuppressWarnings("PMD.ShortMethodName")
    public String id() {
        synchronized (this.lock) {
            return this.origin.id();
        }
    }

    @Override
    public void sendMessage(final String message) {
        synchronized (this.lock) {
            this.origin.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(final byte[] message) {
        synchronized (this.lock) {
            this.origin.sendMessage(message);
        }
    }

    @Override
    public void close() {
        synchronized (this.lock) {
            this.origin.close();
        }
    }

}
