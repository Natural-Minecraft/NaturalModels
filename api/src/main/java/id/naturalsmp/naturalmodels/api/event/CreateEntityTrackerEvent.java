/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.event;

import id.naturalsmp.naturalmodels.api.tracker.EntityTracker;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered when a new {@link EntityTracker} is created.
 * <p>
 * This event allows plugins/mods to perform initialization or tracking logic for entity trackers.
 * </p>
 *
 * @param tracker the newly created entity tracker
 * @since 2.0.0
 */
public record CreateEntityTrackerEvent(
    @NotNull EntityTracker tracker
) implements ModelEvent {
}

