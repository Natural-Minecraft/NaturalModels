/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.script

import id.naturalsmp.naturalmodels.api.NaturalModels
import id.naturalsmp.naturalmodels.api.bone.BoneName
import id.naturalsmp.naturalmodels.api.script.AnimationScript
import id.naturalsmp.naturalmodels.api.tracker.Tracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerUpdateAction
import id.naturalsmp.naturalmodels.api.util.function.BonePredicate

class ChangePartScript(
    val predicate: BonePredicate,
    newModel: String,
    newPart: BoneName
) : AnimationScript {

    private val model by lazy {
        NaturalModels.modelOrNull(newModel)?.groupByTree(newPart)?.itemStack
    }

    override fun accept(tracker: Tracker) {
        model?.let {
            tracker.update(
                TrackerUpdateAction.itemStack(it),
                predicate
            )
        }
    }

    override fun isSync(): Boolean = false
}

