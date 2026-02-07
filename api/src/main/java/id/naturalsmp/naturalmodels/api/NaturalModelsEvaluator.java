/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api;

import id.naturalsmp.naturalmodels.api.util.function.Float2FloatFunction;
import org.jetbrains.annotations.NotNull;

/**
 * Evaluator
 */
public interface NaturalModelsEvaluator {
    /**
     * Compiles molang expression
     * @param expression expression
     * @return compiled function
     */
    @NotNull Float2FloatFunction compile(@NotNull String expression);
}

