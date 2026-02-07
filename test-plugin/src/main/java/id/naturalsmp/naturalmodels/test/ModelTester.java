/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.test;

import org.jetbrains.annotations.NotNull;

public interface ModelTester {
    void start(@NotNull NaturalModelsTest test);
    void end(@NotNull NaturalModelsTest test);
}

