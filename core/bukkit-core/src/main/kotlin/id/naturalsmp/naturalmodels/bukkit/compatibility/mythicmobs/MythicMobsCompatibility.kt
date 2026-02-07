/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs

import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent
import id.naturalsmp.naturalmodels.api.bukkit.NaturalModelsBukkit
import id.naturalsmp.naturalmodels.api.bukkit.entity.BaseBukkitEntity
import id.naturalsmp.naturalmodels.api.script.AnimationScript
import id.naturalsmp.naturalmodels.api.tracker.EntityTracker
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.bukkit.compatibility.Compatibility
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.condition.ModelHasPassengerCondition
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.mechanic.*
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.targeter.ModelPartTargeter
import id.naturalsmp.naturalmodels.manager.ScriptManagerImpl
import id.naturalsmp.naturalmodels.util.*
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MythicMobsCompatibility : Compatibility {

    private companion object {
        const val NAMESPACE = "bm:"
    }

    override fun start() {
        ScriptManagerImpl.addBuilder("mm") { name ->
            val args = name.args() ?: return@addBuilder AnimationScript.EMPTY
            AnimationScript.of(NaturalModelsBukkit.IS_FOLIA) script@ { tracker ->
                if (!CONFIG.module().model) return@script
                if (tracker !is EntityTracker) return@script
                val entity = (tracker.registry().entity() as? BaseBukkitEntity ?: return@script).entity()
                if (!MythicBukkit.inst().apiHelper.castSkill(
                        entity,
                        args,
                    MythicBukkit.inst().apiHelper.getMythicMobInstance(entity)?.power ?: 1F
                ) {
                    name.metadata.toMap().forEach { (key, value) ->
                        it.parameters[key] = value.toString()
                    }
                }) warn(componentOf(
                    "Unknown MythicMobs skill name: ".toComponent(),
                    args.toComponent(NamedTextColor.RED)
                ))
            }
        }
        registerListener(object : Listener {
            @EventHandler
            fun MythicMechanicLoadEvent.load() {
                if (!CONFIG.module().model) return
                when (mechanicName.lowercase().substringAfter(NAMESPACE)) {
                    "playlimbanim" -> register(PlayLimbAnimMechanic(config))
                    "model" -> register(ModelMechanic(config))
                    "state", "animation" -> register(StateMechanic(config))
                    "defaultstate", "defaultanimation" -> register(DefaultStateMechanic(config))
                    "partvisibility", "partvis" -> register(PartVisibilityMechanic(config))
                    "bindhitbox" -> register(BindHitBoxMechanic(config))
                    "changepart" -> register(ChangePartMechanic(config))
                    "tint", "color" -> register(TintMechanic(config))
                    "brightness", "light" -> register(BrightnessMechanic(config))
                    "enchant" -> register(EnchantMechanic(config))
                    "billboard" -> register(BillboardMechanic(config))
                    "glow", "glowbone" -> register(GlowMechanic(config))
                    "mountmodel" -> register(MountModelMechanic(config))
                    "dismountmodel" -> register(DismountModelMechanic(config))
                    "dismountallmodel", "dismountall" -> register(DismountAllModelMechanic(config))
                    "lockmodel", "lockrotation" -> register(LockModelMechanic(config))
                    "bodyrotation", "bodyclamp" -> register(BodyRotationMechanic(config))
                    "remapmodel", "remap" -> register(RemapModelMechanic(config))
                    "pairmodel" -> register(PairModelMechanic(config))
                }
            }

            @EventHandler
            fun MythicConditionLoadEvent.load() {
                if (!CONFIG.module().model) return
                when (conditionName.lowercase().substringAfter(NAMESPACE)) {
                    "modelhaspassenger" -> register(ModelHasPassengerCondition(config))
                }
            }

            @EventHandler
            fun MythicTargeterLoadEvent.load() {
                if (!CONFIG.module().model) return
                when (targeterName.lowercase().substringAfter(NAMESPACE)) {
                    "modelpart" -> register(ModelPartTargeter(config))
                }
            }
        })
    }
}


