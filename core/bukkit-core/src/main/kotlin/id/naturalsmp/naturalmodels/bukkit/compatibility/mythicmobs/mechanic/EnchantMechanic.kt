/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.mechanic

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.*
import id.naturalsmp.naturalmodels.script.EnchantScript

class EnchantMechanic(mlc: MythicLineConfig) : AbstractSkillMechanic(mlc), INoTargetSkill {

    private val model = mlc.modelPlaceholder
    private val predicate = mlc.bonePredicateNullable
    private val enchant = mlc.toPlaceholderBoolean(arrayOf("enchant", "en"), true)

    override fun cast(p0: SkillMetadata): SkillResult {
        val args = p0.toPlaceholderArgs()
        return p0.toTracker(model(args))?.let {
            EnchantScript(
                predicate(args),
                enchant(args)
            ).accept(it)
            SkillResult.SUCCESS
        } ?: SkillResult.CONDITION_FAILED
    }
}


