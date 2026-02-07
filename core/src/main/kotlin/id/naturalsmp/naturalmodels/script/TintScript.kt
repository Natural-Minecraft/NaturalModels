/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.script

import id.naturalsmp.naturalmodels.api.script.AnimationScript
import id.naturalsmp.naturalmodels.api.tracker.EntityTracker
import id.naturalsmp.naturalmodels.api.tracker.Tracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerUpdateAction
import id.naturalsmp.naturalmodels.api.util.function.BonePredicate

class TintScript(
    val predicate: BonePredicate,
    val color: Int,
    val damageTint: Boolean
) : AnimationScript {

    override fun accept(tracker: Tracker) {
        if (damageTint && tracker is EntityTracker) {
            tracker.damageTintValue(color)
        } else {
            if (tracker is EntityTracker) tracker.cancelDamageTint()
            tracker.update(
                TrackerUpdateAction.tint(color),
                predicate
            )
        }
    }

    override fun isSync(): Boolean = false
}
