/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.util

import id.naturalsmp.naturalmodels.api.event.ModelEvent
import id.naturalsmp.naturalmodels.api.util.EventUtil

inline fun <reified T : ModelEvent> callEvent(noinline block: () -> T): Boolean = EventUtil.call(T::class.java) { block() }.triggered()


