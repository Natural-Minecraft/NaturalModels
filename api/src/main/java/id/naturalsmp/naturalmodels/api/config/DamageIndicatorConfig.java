/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.config;

import org.jetbrains.annotations.NotNull;

/**
 * Configuration for damage indicators.
 *
 * @param enabled  whether the feature is enabled
 * @param duration duration in ticks
 * @param format   format of the indicator (e.g., "<red>-<damage>")
 * @param offsetY  vertical offset from the entity's location
 * @since 2.1.0
 */
public record DamageIndicatorConfig(
        boolean enabled,
        int duration,
        @NotNull String format,
        double offsetY) {
    /**
     * Default configuration.
     */
    public static final DamageIndicatorConfig DEFAULT = new DamageIndicatorConfig(false, 20, "<red>-<damage>", 1.5);
}
