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
import org.mockito.Mockito;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests.
 */
@SuppressWarnings("PMD.CloseResource")
final class ServerSocketGuideTest {

    /**
     * The {@link ServerSocket} should be stored into the map using this key.
     */
    private static final String KEY = "serverSocket";

    @Test
    public void serverSocketShouldBeStoredIntoMap() {
        final Map<String, Object> dependencies = new HashMap<>();
        final ServerSocket serverSocket = Mockito.mock(ServerSocket.class);
        new ServerSocketGuide(dependencies).serverSocket(serverSocket);
        MatcherAssert.assertThat(
            dependencies.get("serverSocket"),
            CoreMatchers.equalTo(serverSocket)
        );
    }

    @Test
    public void portShouldBeUsedInServerSocket() {
        final Map<String, Object> dependencies = new HashMap<>();
        final int expected = 333;
        new ServerSocketGuide(dependencies).port(expected);
        final ServerSocket fromMap = (ServerSocket) dependencies.get(KEY);
        MatcherAssert.assertThat(
            fromMap.getLocalPort(),
            CoreMatchers.equalTo(expected)
        );

    }

    @Test
    public void defaultPortShouldBe42069() {
        final Map<String, Object> dependencies = new HashMap<>();
        final int expected = 42_069;
        new ServerSocketGuide(dependencies).defaultPort();
        final ServerSocket fromMap = (ServerSocket) dependencies.get(KEY);
        MatcherAssert.assertThat(
            fromMap.getLocalPort(),
            CoreMatchers.equalTo(expected)
        );
    }

}
