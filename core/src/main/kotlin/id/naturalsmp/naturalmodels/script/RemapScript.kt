/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.script

import id.naturalsmp.naturalmodels.api.NaturalModels
import id.naturalsmp.naturalmodels.api.script.AnimationScript
import id.naturalsmp.naturalmodels.api.tracker.Tracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerUpdateAction
import id.naturalsmp.naturalmodels.util.toPackName
import id.naturalsmp.naturalmodels.util.toSet

class RemapScript(
    model: String,
    map: String?
) : AnimationScript {

    private val newModel by lazy {
        model.toPackName().let {
            NaturalModels.modelOrNull(it)
        }
    }
    private val filter by lazy {
        map?.let {
            NaturalModels.modelOrNull(it.toPackName())?.flatten()?.map { group ->
                group.name()
            }?.toSet()
        }
    }

    override fun accept(tracker: Tracker) {
        val f = filter
        newModel?.run {
            tracker.update(TrackerUpdateAction.perBone {
                (if (f == null || f.contains(it.name())) {
                    groupByTree(it.name())?.itemStack?.let { item ->
                        TrackerUpdateAction.itemStack(item)
                    }
                } else null) ?: TrackerUpdateAction.none()
            })
        }
    }

    override fun isSync(): Boolean = false
}


