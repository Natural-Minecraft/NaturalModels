/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels

import gg.moonflower.molangcompiler.api.MolangCompiler
import gg.moonflower.molangcompiler.api.MolangRuntime
import id.naturalsmp.naturalmodels.api.NaturalModelsEvaluator
import id.naturalsmp.naturalmodels.api.util.function.Float2FloatFunction

class NaturalModelsEvaluatorImpl : NaturalModelsEvaluator {

    private val molang = MolangCompiler.create(MolangCompiler.DEFAULT_FLAGS, javaClass.classLoader)

    private fun Float.query() = MolangRuntime.runtime()
        .setQuery("life_time", this)
        .setQuery("anim_time", this)
        .create()

    override fun compile(expression: String): Float2FloatFunction {
        val compiled = molang.compile(expression)
        return Float2FloatFunction {
            it.query().safeResolve(compiled)
        }
    }
}

