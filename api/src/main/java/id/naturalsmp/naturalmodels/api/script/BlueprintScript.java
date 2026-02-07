/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.script;

import id.naturalsmp.naturalmodels.api.animation.AnimationIterator;
import id.naturalsmp.naturalmodels.api.animation.AnimationModifier;
import id.naturalsmp.naturalmodels.api.animation.TimedStorage;
import id.naturalsmp.naturalmodels.api.data.raw.ModelAnimation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A script data of blueprint.
 * @param name script name
 * @param type type
 * @param length playtime
 * @param scripts scripts
 */
@ApiStatus.Internal
public record BlueprintScript(@NotNull String name, @NotNull AnimationIterator.Type type, float length, @NotNull List<TimeScript> scripts) {

    /**
     * Creates empty script
     * @param animation animation
     * @return empty script
     */
    public static @NotNull BlueprintScript fromEmpty(@NotNull ModelAnimation animation) {
        return new BlueprintScript(
            animation.name(),
            animation.loop(),
            animation.length(),
            List.of(TimeScript.EMPTY, AnimationScript.EMPTY.time(animation.length()))
        );
    }

    /**
     * Creates animation iterator of this script
     * @param modifier modifier
     * @return animation iterator
     */
    public @NotNull AnimationIterator<TimeScript> iterator(@NotNull AnimationModifier modifier) {
        return modifier.type(type).create(TimedStorage.listOf(scripts));
    }
}

