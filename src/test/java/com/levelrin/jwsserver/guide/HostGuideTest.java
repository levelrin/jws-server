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

/**
 * Tests.
 */
final class HostGuideTest {

    /**
     * The host should be stored into the map using this key.
     */
    private static final String KEY = "host";

    @Test
    public void hostShouldBeStoredIntoMap() {
        final Map<String, Object> dependencies = new HashMap<>();
        new HostGuide(dependencies).host("levelrin.com");
        MatcherAssert.assertThat(
            dependencies.get(KEY),
            CoreMatchers.equalTo("levelrin.com")
        );
    }

    @Test
    public void localhostShouldBeStoredIntoMap() {
        final Map<String, Object> dependencies = new HashMap<>();
        new HostGuide(dependencies).localhost();
        MatcherAssert.assertThat(
            dependencies.get(KEY),
            CoreMatchers.equalTo("localhost")
        );
    }

    @Test
    public void hostShouldBeEmptyIfUserSkipsValidation() {
        final Map<String, Object> dependencies = new HashMap<>();
        new HostGuide(dependencies).skipHostValidation();
        MatcherAssert.assertThat(
            dependencies.get(KEY),
            CoreMatchers.equalTo("")
        );
    }

}
