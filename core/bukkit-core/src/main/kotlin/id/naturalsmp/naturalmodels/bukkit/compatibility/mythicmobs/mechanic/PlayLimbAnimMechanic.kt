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
import id.naturalsmp.naturalmodels.api.NaturalModels
import id.naturalsmp.naturalmodels.api.animation.AnimationIterator
import id.naturalsmp.naturalmodels.api.animation.AnimationModifier
import id.naturalsmp.naturalmodels.api.util.function.FloatConstantSupplier
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.*
import id.naturalsmp.naturalmodels.bukkit.util.wrap
import id.naturalsmp.naturalmodels.util.componentOf
import id.naturalsmp.naturalmodels.util.toComponent
import id.naturalsmp.naturalmodels.util.warn
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class PlayLimbAnimMechanic(mlc: MythicLineConfig) : AbstractSkillMechanic(mlc), ITargetedEntitySkill {

    private val modelId = mlc.modelPlaceholder
    private val animationId = mlc.toPlaceholderString(arrayOf("animation", "anim", "a"))
    private val speed = mlc.toNullablePlaceholderFloat(arrayOf("speed", "sp"))
    private val remove = mlc.toPlaceholderBoolean(arrayOf("remove", "r"), false)
    private val mode = mlc.toPlaceholderString(arrayOf("mode", "loop"), "once") {
        when (it?.lowercase()) {
            "loop" -> AnimationIterator.Type.LOOP
            "hold" -> AnimationIterator.Type.HOLD_ON_LAST
            else -> AnimationIterator.Type.PLAY_ONCE
        }
    }

    override fun castAtEntity(data: SkillMetadata, target: AbstractEntity): SkillResult {
        val targetPlayer = target.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED
        val args = toPlaceholderArgs(data, target)

        val removal = remove(args)
        val currentModelId = modelId(args) ?: return SkillResult.INVALID_CONFIG
        val currentAnimationId = animationId(args) ?: if (!removal) return SkillResult.INVALID_CONFIG else ""

        if (removal) {
            NaturalModels.registryOrNull(targetPlayer.uniqueId)?.remove(currentModelId)
        } else {
            val renderer = NaturalModels.limb(currentModelId).orElse(null) ?: return SkillResult.CONDITION_FAILED.apply {
                warn(componentOf(
                    "Error: Player not found: ".toComponent(),
                    currentModelId.toComponent(NamedTextColor.RED)
                ))
            }
            val loopType = mode(args)
            val modifier = AnimationModifier(0, 0, loopType, speed(args)?.let(FloatConstantSupplier::of))
            renderer.getOrCreate(targetPlayer.wrap()).run {
                if (!animate(
                        currentAnimationId,
                        modifier,
                        if (loopType == AnimationIterator.Type.PLAY_ONCE) {
                            { close() }
                        } else {
                            {}
                        }
                )) close()
            }

        }
        return SkillResult.SUCCESS
    }
}


