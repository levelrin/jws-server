/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import com.levelrin.jwsserver.reaction.Reaction;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests.
 */
final class ReactionGuideTest {

    @Test
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED")
    public void reactionShouldBeAddedIntoList() {
        final List<Reaction> reactions = new ArrayList<>();
        final Reaction reaction = Mockito.mock(Reaction.class);
        new ReactionGuide(
            new HashMap<>(),
            reactions
        ).reaction(reaction);
        MatcherAssert.assertThat(
            reactions.get(0),
            CoreMatchers.equalTo(reaction)
        );
    }

    @Test
    public void reactionListShouldBeStoredIntoMap() {
        final Map<String, Object> dependencies = new HashMap<>();
        final List<Reaction> reactions = new ArrayList<>();
        new ReactionGuide(
            dependencies,
            reactions
        ).ready();
        MatcherAssert.assertThat(
            dependencies.get("reactions"),
            CoreMatchers.equalTo(reactions)
        );
    }

}
