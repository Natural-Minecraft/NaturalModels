/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.script

import id.naturalsmp.naturalmodels.api.script.AnimationScript
import id.naturalsmp.naturalmodels.api.tracker.Tracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerUpdateAction
import id.naturalsmp.naturalmodels.api.util.function.BonePredicate

class BrightnessScript(
    val predicate: BonePredicate,
    val block: Int,
    val sky: Int
) : AnimationScript {

    override fun accept(tracker: Tracker) {
        tracker.update(
            TrackerUpdateAction.brightness(block, sky),
            predicate
        )
    }

    override fun isSync(): Boolean = false
}


