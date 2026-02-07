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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered when a player's skin data is created or loaded.
 * <p>
 * This event allows modifying the player's model profile before it is used.
 * </p>
 *
 * @since 2.0.0
 */
@Getter
@Setter
public final class CreatePlayerSkinEvent implements ModelEvent {

    private ModelProfile modelProfile;

    /**
     * Creates a new CreatePlayerSkinEvent.
     *
     * @param modelProfile the model profile being created
     * @since 2.0.0
     */
    @ApiStatus.Internal
    public CreatePlayerSkinEvent(@NotNull ModelProfile modelProfile) {
        this.modelProfile = modelProfile;
    }
}

