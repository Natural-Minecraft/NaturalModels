/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.data.blueprint;

import com.google.gson.JsonObject;
import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.data.raw.ModelResolution;
import id.naturalsmp.naturalmodels.api.pack.PackObfuscator;
import id.naturalsmp.naturalmodels.api.util.json.JsonObjectBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a processed texture in a model blueprint.
 * <p>
 * This record holds the texture's name, binary image data, dimensions, and rendering properties.
 * </p>
 *
 * @param name the internal name of the texture
 * @param image the binary content of the texture image
 * @param width the original width of the texture in pixels
 * @param height the original height of the texture in pixels
 * @param uvWidth the UV width of the texture, if specified
 * @param uvHeight the UV height of the texture, if specified
 * @param canBeRendered whether this texture should be included in the resource pack
 * @param frameTime the frame time of the texture
 * @param frameInterpolate the interpolation flag of the texture
 * @since 1.15.2
 */
public record BlueprintTexture(
    @NotNull String name,
    byte[] image,
    int width,
    int height,
    int uvWidth,
    int uvHeight,
    boolean canBeRendered,
    int frameTime,
    boolean frameInterpolate
) {
    /**
     * Checks if this texture is an animated texture (a texture atlas for animation).
     *
     * @return true if it is an animated texture, false otherwise
     * @since 1.15.2
     */
    public boolean isAnimatedTexture() {
        if (hasUVSize()) {
            var h = (float) height / uvHeight;
            var w = (float) width / uvWidth;
            return h > w;
        } else {
            return height > 0 && width > 0 && height / width > 1;
        }
    }

    /**
     * Generates the .mcmeta file content for this texture if it is animated.
     *
     * @return the JSON object for the .mcmeta file
     * @since 1.15.2
     */
    public @NotNull JsonObject toMcmeta() {
        return JsonObjectBuilder.builder()
            .jsonObject("animation", animation -> {
                animation.property("interpolate", frameInterpolate());
                animation.property("frametime", frameTime());
            })
            .build();
    }

    /**
     * Generates the pack-compliant file name for this texture.
     *
     * @param obfuscator the obfuscator to use for the name
     * @return the obfuscated file name
     * @since 1.15.2
     */
    public @NotNull String packName(@NotNull PackObfuscator obfuscator) {
        return obfuscator.obfuscate(name());
    }

    /**
     * Generates the full resource pack namespace path for this texture.
     *
     * @param obfuscator the obfuscator to use for the name
     * @return the texture's namespace path
     * @since 1.15.2
     */
    public @NotNull String packNamespace(@NotNull PackObfuscator obfuscator) {
        return NaturalModels.config().namespace() + ":item/" + packName(obfuscator);
    }

    /**
     * Checks if this texture has a specific UV size defined.
     *
     * @return true if UV width and height are specified, false otherwise
     * @since 1.15.2
     */
    public boolean hasUVSize() {
        return uvWidth > 0 && uvHeight > 0;
    }

    /**
     * Returns the effective resolution for this texture's UV mapping.
     *
     * @param resolution the parent model's resolution
     * @return the UV resolution, or the parent resolution if not specified
     * @since 1.15.2
     */
    public @NotNull ModelResolution resolution(@NotNull ModelResolution resolution) {
        if (!hasUVSize()) return resolution;
        return resolution.width() == width && resolution.height() == height ? resolution : new ModelResolution(uvWidth, uvHeight);
    }
}

