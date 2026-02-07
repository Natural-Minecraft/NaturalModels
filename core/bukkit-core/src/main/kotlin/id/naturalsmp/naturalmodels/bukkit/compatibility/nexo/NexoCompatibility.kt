/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.compatibility.nexo

import com.nexomc.nexo.api.events.resourcepack.NexoPrePackGenerateEvent
import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.bukkit.compatibility.Compatibility
import id.naturalsmp.naturalmodels.bukkit.util.PLUGIN
import id.naturalsmp.naturalmodels.util.*
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class NexoCompatibility : Compatibility {
    override fun start() {
        if (CONFIG.mergeWithExternalResources()) PLUGIN.skipInitialReload()
        registerListener(object : Listener {
            @EventHandler
            fun NexoPrePackGenerateEvent.generate() {
                if (!CONFIG.mergeWithExternalResources()) return
                when (val result = PLATFORM.reload()) {
                    is NaturalModelsPlatform.ReloadResult.Success -> {
                        result.packResult().directory()?.let {
                            addResourcePack(it)
                            info("Successfully merged with Nexo.".toComponent(NamedTextColor.GREEN))
                        }
                    }

                    is NaturalModelsPlatform.ReloadResult.OnReload -> {
                        warn("NaturalModels is still on reload!".toComponent(NamedTextColor.RED))
                    }

                    is NaturalModelsPlatform.ReloadResult.Failure -> {
                        result.throwable.handleException("Unable to merge with Nexo.")
                    }
                }
            }
        })
    }
}


