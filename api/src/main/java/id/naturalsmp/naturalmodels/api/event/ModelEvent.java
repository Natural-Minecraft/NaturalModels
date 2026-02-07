/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.event;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.NaturalModelsEventBus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a base event in the NaturalModels system.
 * <p>
 * All events related to model lifecycle, interaction, and animation implement this interface.
 * Events can be dispatched using the {@link #call()} method.
 * </p>
 *
 * @since 2.0.0
 */
public interface ModelEvent {

    /**
     * Dispatches this event to the global event bus.
     *
     * @return the result of the event call
     * @since 2.0.0
     */
    default @NotNull NaturalModelsEventBus.Result call() {
        return NaturalModels.eventBus().call(this);
    }
}

