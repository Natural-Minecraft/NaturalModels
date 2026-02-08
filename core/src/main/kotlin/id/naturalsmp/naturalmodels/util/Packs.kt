/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.util

import id.naturalsmp.naturalmodels.api.NaturalModelsConfig
import id.naturalsmp.naturalmodels.api.NaturalModelsConfig.PackType.*
import id.naturalsmp.naturalmodels.api.pack.*
import id.naturalsmp.naturalmodels.manager.ModelManagerImpl
import id.naturalsmp.naturalmodels.manager.ReloadPipeline
import net.kyori.adventure.text.format.NamedTextColor
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
import java.util.zip.Deflater
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.pathString

fun NaturalModelsConfig.PackType.toGenerator() = when (this) {
    FOLDER -> FolderGenerator()
    ZIP -> ZipGenerator()
    ITEMSADDER -> ItemsAdderGenerator()
    NONE -> NoneGenerator()
}

class ItemsAdderGenerator : PackGenerator {
    private val iaData = File(DATA_FOLDER.parent, "ItemsAdder/data/resource_pack")
    override val exists: Boolean = iaData.exists()

    override fun create(zipper: PackZipper, pipeline: ReloadPipeline): PackResult {
        val build = zipper.build()
        val pack = PackResult(build.meta(), iaData)
        val changed = AtomicBoolean()

        pipeline.forEachParallel(build.resources(), PackResource::estimatedSize) {
            val bytes = it.get()
            pack[it.overlay()] = PackByte(it.path(), bytes)
            
            val relativePath = it.path().path
            val targetFile = File(iaData, "assets/$relativePath")
            
            if (!targetFile.exists() || targetFile.length() != bytes.size.toLong()) {
                targetFile.parentFile.mkdirs()
                targetFile.writeBytes(bytes)
                changed.set(true)
                debugPack {
                    componentOf(
                        "Exported to ItemsAdder: ".toComponent(),
                        relativePath.toComponent(NamedTextColor.GREEN)
                    )
                }
            }
        }
        
        return pack.apply {
            freeze(changed.get())
        }.also {
            generateModelsYml()
        }
    }

    private fun generateModelsYml() {
        val modelsFile = File(DATA_FOLDER.parent, "ItemsAdder/contents/naturalmodels/configs/models.yml")
        val ns = CONFIG.namespace()
        val itemMaterial = CONFIG.itemModel()
        
        val sb = StringBuilder()
        sb.append("info:\n")
        sb.append("  namespace: ").append(ns).append("\n")
        sb.append("items:\n")

        ModelManagerImpl.modelIdMap.forEach { (name, id) ->
            sb.append("  ").append(name).append(":\n")
            sb.append("    display_name: \"").append(name).append("\"\n")
            sb.append("    permission: \"").append(ns).append(".model.").append(name).append("\"\n")
            sb.append("    resource:\n")
            sb.append("      material: ").append(itemMaterial).append("\n")
            sb.append("      generate: false\n")
            sb.append("      model_path: \"").append(ns).append(":modern_item/").append(name).append("\"\n")
            sb.append("      model_id: ").append(id).append("\n")
        }

        if (!modelsFile.exists() || modelsFile.readText() != sb.toString()) {
            modelsFile.parentFile.mkdirs()
            modelsFile.writeText(sb.toString())
            info(
                net.kyori.adventure.text.Component.text("Generated ItemsAdder models.yml at: " + modelsFile.path, NamedTextColor.GREEN)
            )
        }
    }
}

interface PackGenerator {
    val exists: Boolean
    fun create(zipper: PackZipper, pipeline: ReloadPipeline): PackResult
    fun hashEquals(result: PackResult): Boolean {
        val hash = result.hash().toString()
        return File(DATA_FOLDER.getOrCreateDirectory(".cache"), "zip-hash.txt").run {
            if (!exists || !exists() || readText() != hash) {
                writeText(hash)
                true
            } else false
        }
    }
}

class FolderGenerator : PackGenerator {
    private val file = File(DATA_FOLDER.parent, CONFIG.buildFolderLocation())
    override val exists: Boolean = file.exists()
    private val fileTree by lazy {
        sortedMapOf<String, Path>(reverseOrder()).apply {
            val after = CONFIG.buildFolderLocation() + File.separatorChar
            Files.walk(file.apply {
                mkdirs()
            }.toPath()).use { stream ->
                stream.forEach {
                    put(it.pathString.substringAfter(after), it)
                }
            }
        }
    }

    private fun PackPath.toFile(): File {
        val replaced = path.replace('/', File.separatorChar)
        return synchronized(fileTree) {
            fileTree.remove(replaced)?.toFile()
        } ?: File(file, replaced).apply {
            parentFile.mkdirs()
        }
    }

    override fun create(zipper: PackZipper, pipeline: ReloadPipeline): PackResult {
        val build = zipper.build()
        val pack = PackResult(build.meta(), file)
        val changed = AtomicBoolean()
        pipeline.forEachParallel(build.resources(), PackResource::estimatedSize) {
            val bytes = it.get()
            pack[it.overlay()] = PackByte(it.path(), bytes)
            val file = it.path().toFile()
            val index = pipeline.progress()
            if (file.length() != bytes.size.toLong()) {
                file.writeBytes(bytes)
                changed.set(true)
                debugPack {
                    componentOf(
                        "This file was successfully generated: ".toComponent(),
                        it.path().path.toComponent(NamedTextColor.GREEN),
                        " ($index/${pipeline.goal})".toComponent(NamedTextColor.DARK_GRAY)
                    )
                }
            }
        }
        fileTree.values.forEach {
            it.toFile().delete()
        }
        return pack.apply {
            freeze(changed.get())
        }
    }
}

class ZipGenerator : PackGenerator {
    private val file = File(DATA_FOLDER.parent, "${CONFIG.buildFolderLocation()}.zip")
    override val exists: Boolean = file.exists()

    override fun create(zipper: PackZipper, pipeline: ReloadPipeline): PackResult {
        return zipper.writeToResult(pipeline, file).apply {
            freeze(hashEquals(this))
        }.apply {
            if (!changed()) return this
            fun zip(zip: ZipOutputStream) {
                zip.setLevel(Deflater.BEST_COMPRESSION)
                zip.setComment("NaturalModels's generated resource pack.")
                stream().forEach {
                    zip.putNextEntry(ZipEntry(it.path().path()))
                    zip.write(it.bytes())
                    zip.closeEntry()
                }
            }
            file.outputStream().use {
                it.buffered().use { buffered ->
                    ZipOutputStream(buffered).use(::zip)
                }
            }
        }
    }
}

class NoneGenerator : PackGenerator {
    override val exists: Boolean = false
    override fun create(zipper: PackZipper, pipeline: ReloadPipeline): PackResult {
        return zipper.writeToResult(pipeline).apply {
            freeze()
        }
    }
}

fun PackZipper.writeToResult(pipeline: ReloadPipeline, dir: File? = null): PackResult {
    val build = build()
    return PackResult(build.meta(), dir).apply {
        pipeline.forEachParallel(build.resources(), PackResource::estimatedSize) {
            set(it.overlay(), PackByte(it.path(), it.get()))
            val index = pipeline.progress()
            debugPack {
                componentOf(
                    "This file was successfully zipped: ".toComponent(),
                    it.path().path.toComponent(NamedTextColor.GREEN),
                    " ($index/${pipeline.goal})".toComponent(NamedTextColor.DARK_GRAY)
                )
            }
        }
    }
}
