/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import com.levelrin.jwsserver.reaction.Reaction;
import java.util.List;
import java.util.Map;

/**
 * Please check the definition of {@link Reaction}.
 * It's for setting the reactions.
 */
public final class ReactionGuide {

    /**
     * We will store all the dependencies for composing objects.
     */
    private final Map<String, Object> dependencies;

    /**
     * List of reactions that we are going to use.
     */
    private final List<Reaction> reactions;

    /**
     * Constructor.
     * @param dependencies See {@link ReactionGuide#dependencies}.
     * @param reactions See {@link ReactionGuide#reactions}.
     */
    public ReactionGuide(final Map<String, Object> dependencies, final List<Reaction> reactions) {
        this.dependencies = dependencies;
        this.reactions = reactions;
    }

    /**
     * Add a reaction into the server.
     * @param reaction Please check the definition of {@link Reaction}.
     * @return It returns the same type so that you can add more reactions.
     */
    public ReactionGuide reaction(final Reaction reaction) {
        this.reactions.add(reaction);
        return new ReactionGuide(this.dependencies, this.reactions);
    }

    /**
     * Finish setting dependencies.
     * The last step is to compose all the objects using the dependencies.
     * @return The guide for composing objects at last.
     */
    public FinalGuide ready() {
        this.dependencies.put("reactions", this.reactions);
        return new FinalGuide(this.dependencies);
    }

}
