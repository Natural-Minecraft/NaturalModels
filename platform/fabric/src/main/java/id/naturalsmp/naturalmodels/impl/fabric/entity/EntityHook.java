/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric.entity;

import org.jetbrains.annotations.Nullable;

public interface EntityHook {
    @Nullable String NaturalModels$getModelData();

    void NaturalModels$setModelData(@Nullable String modelData);
}

