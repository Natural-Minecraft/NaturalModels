/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.entity;

import id.naturalsmp.naturalmodels.api.nms.Profiled;
import id.naturalsmp.naturalmodels.api.platform.PlatformPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * An adapter of player
 */
public interface BasePlayer extends BaseEntity, Profiled {

    /**
     * Updates current inventory
     */
    void updateInventory();

    @Override
    @NotNull PlatformPlayer platform();
}

