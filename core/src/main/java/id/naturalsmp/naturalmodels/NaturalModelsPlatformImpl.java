/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels;

import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform;
import id.naturalsmp.naturalmodels.manager.ReloadPipeline;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.function.BiConsumer;

public interface NaturalModelsPlatformImpl extends NaturalModelsPlatform {

    void saveResource(@NotNull String resourcePath);

    void loadAssets(@NotNull ReloadPipeline pipeline, @NotNull String prefix, @NotNull BiConsumer<String, InputStream> consumer);
}

