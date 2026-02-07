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
 * Triggered when a per-player animation sequence starts.
 * <p>
 * This event signifies that a specific animation is beginning to play for only one player.
 * </p>
 *
 * @param tracker the tracker playing the animation
 * @param player the player viewing the animation
 * @since 2.0.0
 */
public record PlayerPerAnimationStartEvent(
    @NotNull Tracker tracker,
    @NotNull PlatformPlayer player
) implements ModelEvent {
}

