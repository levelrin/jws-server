/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.option;

import com.levelrin.jwsserver.session.advanced.AdvancedClose;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.net.Socket;

/**
 * Tests.
 */
final class CloseOptionTest {

    @Test
    public void shouldReturnAdvancedClose() {
        MatcherAssert.assertThat(
            new CloseOption().apply(Mockito.mock(Socket.class)),
            CoreMatchers.instanceOf(AdvancedClose.class)
        );
    }

}
