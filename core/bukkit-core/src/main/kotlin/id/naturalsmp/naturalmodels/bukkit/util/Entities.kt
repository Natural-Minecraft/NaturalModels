/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.util

import id.naturalsmp.naturalmodels.api.NaturalModels
import org.bukkit.entity.Entity

fun Entity.toTracker(model: String?) = toRegistry()?.tracker(model)
fun Entity.toRegistry() = NaturalModels.registryOrNull(uniqueId)


