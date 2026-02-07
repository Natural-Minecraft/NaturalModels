/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bone;

import org.jetbrains.annotations.NotNull;

public interface BoneEventHandler {
    @NotNull BoneEventDispatcher eventDispatcher();

    default void extend(@NotNull BoneEventHandler eventHandler) {
        eventDispatcher().extend(eventHandler.eventDispatcher());
    }
}

