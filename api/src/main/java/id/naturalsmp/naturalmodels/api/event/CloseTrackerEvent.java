/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.event;

import id.naturalsmp.naturalmodels.api.tracker.Tracker;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered when a tracker is closed.
 * <p>
 * This event provides information about the tracker being closed and the reason for closure.
 * </p>
 *
 * @param tracker the tracker being closed
 * @param reason the reason for closing the tracker
 * @since 2.0.0
 */
public record CloseTrackerEvent(
    @NotNull Tracker tracker,
    @NotNull Tracker.CloseReason reason
) implements ModelEvent {
}

