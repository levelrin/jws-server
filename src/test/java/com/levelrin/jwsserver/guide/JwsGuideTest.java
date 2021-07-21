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
final class JwsGuideTest {

    /**
     * The {@link ExecutorService} should be stored into the map using this key.
     */
    private static final String KEY = "serverThread";

    @Test
    public void serverThreadShouldBeStoredIntoMap() {
        final Map<String, Object> dependencies = new HashMap<>();
        final ExecutorService service = Executors.newCachedThreadPool();
        new JwsGuide(dependencies).serverThread(service);
        MatcherAssert.assertThat(
            dependencies.get(KEY),
            CoreMatchers.equalTo(service)
        );
    }

    @Test
    public void defaultServerThreadShouldBeStoredIntoMap() {
        final Map<String, Object> dependencies = new HashMap<>();
        new JwsGuide(dependencies).defaultServerThread();
        MatcherAssert.assertThat(
            dependencies.get(KEY),
            CoreMatchers.instanceOf(ExecutorService.class)
        );
    }

}
