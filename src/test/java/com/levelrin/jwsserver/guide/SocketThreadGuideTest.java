/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Tests.
 */
final class SocketThreadGuideTest {

    /**
     * The {@link ExecutorService} should be stored into the map using this key.
     */
    private static final String KEY = "socketThread";

    @Test
    public void socketThreadShouldBeStoredIntoMap() {
        final Map<String, Object> dependencies = new HashMap<>();
        final ExecutorService service = Executors.newCachedThreadPool();
        new SocketThreadGuide(dependencies).socketThread(service);
        MatcherAssert.assertThat(
            dependencies.get(KEY),
            CoreMatchers.equalTo(service)
        );
    }

    @Test
    public void defaultSocketThreadShouldBeSameAsServerThread() {
        final Map<String, Object> dependencies = new HashMap<>();
        final ExecutorService service = Executors.newCachedThreadPool();
        dependencies.put("serverThread", service);
        new SocketThreadGuide(dependencies).defaultSocketThread();
        MatcherAssert.assertThat(
            dependencies.get(KEY),
            CoreMatchers.equalTo(service)
        );
    }

}
