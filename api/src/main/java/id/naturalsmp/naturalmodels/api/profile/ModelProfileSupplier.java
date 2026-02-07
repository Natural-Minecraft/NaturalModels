/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.profile;

import id.naturalsmp.naturalmodels.api.platform.PlatformOfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Profile supplier
 */
public interface ModelProfileSupplier {

    /**
     * Supplies profile
     * @param info info
     * @return uncompleted profile
     */
    @NotNull ModelProfile.Uncompleted supply(@NotNull ModelProfileInfo info);

    /**
     * Supplies profile by player
     * @param player player
     * @return uncompleted profile
     */
    default @NotNull ModelProfile.Uncompleted supply(@NotNull PlatformOfflinePlayer player) {
        return supply(new ModelProfileInfo(player.uuid(), player.name()));
    }
}

