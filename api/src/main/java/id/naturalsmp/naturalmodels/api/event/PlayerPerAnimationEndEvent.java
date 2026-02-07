/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.event;

import id.naturalsmp.naturalmodels.api.platform.PlatformPlayer;
import id.naturalsmp.naturalmodels.api.tracker.Tracker;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered when a per-player animation sequence ends.
 * <p>
 * This event signifies that a specific animation playing only for one player has finished.
 * </p>
 *
 * @param tracker the tracker playing the animation
 * @param player the player who viewed the animation
 * @since 2.0.0
 */
public record PlayerPerAnimationEndEvent(
    @NotNull Tracker tracker,
    @NotNull PlatformPlayer player
) implements ModelEvent {
}

