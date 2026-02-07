/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.event;


import id.naturalsmp.naturalmodels.api.data.blueprint.ModelBlueprint;
import id.naturalsmp.naturalmodels.api.data.renderer.ModelRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered when a model is successfully imported and registered.
 * <p>
 * This event provides access to the raw blueprint and the created renderer.
 * </p>
 *
 * @param blueprint the model blueprint
 * @param renderer the model renderer
 * @since 2.0.0
 */
public record ModelImportedEvent(
    @NotNull ModelBlueprint blueprint,
    @NotNull ModelRenderer renderer
) implements ModelEvent {
}

