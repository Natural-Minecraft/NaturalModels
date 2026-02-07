/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.manager.debug

import id.naturalsmp.naturalmodels.manager.ReloadPipeline

interface ReloadIndicator {
    infix fun status(status: ReloadPipeline.Status)
    fun close()
}
