/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.option;

import com.levelrin.jwsserver.session.advanced.Ping;
import java.net.Socket;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests.
 */
final class PingOptionTest {

    @Test
    public void shouldReturnPing() {
        MatcherAssert.assertThat(
            new PingOption().apply(
                Mockito.mock(Socket.class)
            ),
            CoreMatchers.instanceOf(Ping.class)
        );
    }

}
