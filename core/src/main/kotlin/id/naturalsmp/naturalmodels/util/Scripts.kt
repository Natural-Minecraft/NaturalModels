/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.util

import id.naturalsmp.naturalmodels.api.script.ScriptBuilder
import id.naturalsmp.naturalmodels.api.util.function.BonePredicate

val ScriptBuilder.ScriptMetaData.bonePredicate get(): BonePredicate {
    val match = asBoolean("exact") != false
    val children = asBoolean("children") == true
    val part = asString("part")?.boneName?.name
    return if (part == null) BonePredicate.TRUE else {
        BonePredicate.of(if (children) BonePredicate.State.TRUE else BonePredicate.State.FALSE, if (match) {
            { b ->
                b.name().name == part
            }
        } else {
            { b ->
                b.name().name.contains(part, ignoreCase = true)
            }
        })
    }
}
