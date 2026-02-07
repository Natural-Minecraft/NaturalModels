/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.data.raw;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the texture resolution of a model.
 *
 * @param width the width of the texture in pixels
 * @param height the height of the texture in pixels
 * @since 1.15.2
 */
@ApiStatus.Internal
public record ModelResolution(
    int width,
    int height
) {
}

