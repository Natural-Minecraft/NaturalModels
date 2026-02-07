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
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.modelPlaceholder
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.toPlaceholderArgs
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.toPlaceholderBoolean
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.toTracker

class LockModelMechanic(mlc: MythicLineConfig) : AbstractSkillMechanic(mlc), INoTargetSkill {

    private val model = mlc.modelPlaceholder
    private val lock = mlc.toPlaceholderBoolean(arrayOf("lock", "l"), true)

    override fun cast(p0: SkillMetadata): SkillResult {
        val args = p0.toPlaceholderArgs()
        return p0.toTracker(model(args))?.bodyRotator()?.let {
            it.lockRotation(lock(args))
            SkillResult.SUCCESS
        } ?: SkillResult.CONDITION_FAILED
    }
}


