/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.mechanic

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.modelPlaceholder
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.toPlaceholderArgs
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.toPlaceholderBoolean
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.toTracker
import id.naturalsmp.naturalmodels.bukkit.util.wrap
import org.bukkit.entity.Player

class PairModelMechanic(mlc: MythicLineConfig) : AbstractSkillMechanic(mlc), ITargetedEntitySkill {

    private val model = mlc.modelPlaceholder
    private val remove = mlc.toPlaceholderBoolean(arrayOf("remove", "r"), false)

    override fun castAtEntity(p0: SkillMetadata, p1: AbstractEntity): SkillResult {
        val target = p1.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED
        val args = toPlaceholderArgs(p0, p1)
        p0.toTracker(model(args))?.let {
            if (remove(args)) {
                it.show(target.wrap())
            } else {
                it.hide(target.wrap())
            }
        }
        return SkillResult.SUCCESS
    }
}


