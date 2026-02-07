/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.compatibility.citizens.trait

import id.naturalsmp.naturalmodels.api.NaturalModels
import id.naturalsmp.naturalmodels.api.data.renderer.ModelRenderer
import id.naturalsmp.naturalmodels.bukkit.util.wrap
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitName
import net.citizensnpcs.api.util.DataKey

@TraitName("model")
class ModelTrait : Trait("model") {
    private var _renderer: ModelRenderer? = null
    var renderer
        get() = _renderer
        set(value) {
            npc?.entity?.let {
                value?.create(it.wrap()) ?: NaturalModels.registryOrNull(it.uniqueId)?.close()
            }
            _renderer = value
        }

    override fun load(key: DataKey) {
        key.getString("")?.let {
            NaturalModels.modelOrNull(it)?.let { model ->
                renderer = model
            }
        }
    }

    override fun save(key: DataKey) {
        npc?.entity?.uniqueId?.let { uuid ->
            key.setString("", NaturalModels.registryOrNull(uuid)?.first()?.name())
        }
    }

    override fun onSpawn() {
        npc?.entity?.let {
            if (NaturalModels.registryOrNull(it.uniqueId) == null) {
                renderer?.create(it.wrap())
            }
        }
    }

    override fun onCopy() {
        onSpawn()
    }

    override fun onDespawn() {
        npc?.entity?.uniqueId?.let {
            NaturalModels.registryOrNull(it)?.close()
        }
    }

    override fun onRemove() {
        npc?.entity?.uniqueId?.let {
            NaturalModels.registryOrNull(it)?.close()
        }
    }
}


