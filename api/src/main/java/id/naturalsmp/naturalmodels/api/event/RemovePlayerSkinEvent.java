/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.event;

import id.naturalsmp.naturalmodels.api.profile.ModelProfile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered when a player's skin data is about to be removed from the cache.
 * <p>
 * This event allows cancelling the removal to keep the skin data cached.
 * </p>
 *
 * @since 2.0.0
 */
@Getter
@Setter
public final class RemovePlayerSkinEvent implements CancellableEvent {
    private final ModelProfile modelProfile;
    private boolean cancelled;

    /**
     * Creates a new RemovePlayerSkinEvent.
     *
     * @param modelProfile the model profile being removed
     * @since 2.0.0
     */
    public RemovePlayerSkinEvent(@NotNull ModelProfile modelProfile) {
        this.modelProfile = modelProfile;
    }
}

