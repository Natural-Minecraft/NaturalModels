/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * NaturalModels's logger
 */
public interface NaturalModelsLogger {
    /**
     * Infos messages
     * @param message message
     */
    void info(@NotNull Component... message);

    /**
     * Warns message
     * @param message message
     */
    void warn(@NotNull Component... message);
}

