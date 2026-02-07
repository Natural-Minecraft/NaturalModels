/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.manager

import id.naturalsmp.naturalmodels.api.pack.PackZipper

interface GlobalManager {
    fun start() {}
    fun reload(pipeline: ReloadPipeline, zipper: PackZipper)
    fun end() {}
}

