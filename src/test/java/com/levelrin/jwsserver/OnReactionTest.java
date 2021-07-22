/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.reaction.Reaction;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class OnReactionTest {

    @Test
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED")
    public void shouldFindReactionThatBelongsToEndpoint() {
        final Reaction yoiEndpoint = Mockito.mock(Reaction.class);
        Mockito.doReturn("/yoi").when(yoiEndpoint).endpoint();
        final Reaction defaultEndpoint = Mockito.mock(Reaction.class);
        Mockito.doReturn("/").when(defaultEndpoint).endpoint();
        final AtomicReference<Reaction> cache = new AtomicReference<>();
        new OnReaction(
            Collections.singletonList("GET / HTTP/1.1"),
            Arrays.asList(yoiEndpoint, defaultEndpoint),
            chosenOne -> {
                cache.set(chosenOne);
                return Mockito.mock(WsServer.class);
            }
        ).start();
        MatcherAssert.assertThat(
            cache.get().endpoint(),
            CoreMatchers.equalTo("/")
        );
    }

}
