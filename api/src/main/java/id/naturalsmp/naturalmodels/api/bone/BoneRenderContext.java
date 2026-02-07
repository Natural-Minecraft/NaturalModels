/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bone;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.data.renderer.RenderSource;
import id.naturalsmp.naturalmodels.api.skin.SkinData;
import org.jetbrains.annotations.NotNull;

/**
 * Render item context
 * @param source source
 * @param skin skin
 */
public record BoneRenderContext(@NotNull RenderSource<?> source, @NotNull SkinData skin) {

    /**
     * Creates default context
     * @param source source
     */
    public BoneRenderContext(@NotNull RenderSource<?> source) {
        this(source, NaturalModels.platform().skinManager().fallback());
    }
}

