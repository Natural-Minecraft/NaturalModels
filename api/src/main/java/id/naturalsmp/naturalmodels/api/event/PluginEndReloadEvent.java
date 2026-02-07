/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.event;

import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered when the NaturalModels platform finishes reloading.
 * <p>
 * This event provides the result of the reload operation.
 * </p>
 *
 * @param result the result of the reload
 * @since 2.0.0
 */
public record PluginEndReloadEvent(
    @NotNull NaturalModelsPlatform.ReloadResult result
) implements ModelEvent {
}

