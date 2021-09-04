/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import com.levelrin.jwsserver.session.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests.
 */
final class PongGuideTest {

    @Test
    public void shouldPutEmptyConsumerOntoDependencies() {
        final Map<String, Object> dependencies = new HashMap<>();
        new PongGuide(dependencies).ignorePong();
        MatcherAssert.assertThat(
            dependencies.get("onPong"),
            CoreMatchers.instanceOf(Consumer.class)
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldPutConsumerOntoDependencies() {
        final Map<String, Object> dependencies = new HashMap<>();
        final AtomicReference<String> actual = new AtomicReference<>();
        new PongGuide(dependencies).onPong(session -> actual.set(session.id()));
        final Consumer<Session> onPong = (Consumer<Session>) dependencies.get("onPong");
        final Session session = Mockito.mock(Session.class);
        Mockito.doReturn("fakeId").when(session).id();
        onPong.accept(session);
        MatcherAssert.assertThat(
            actual.get(),
            CoreMatchers.equalTo("fakeId")
        );
    }

}
