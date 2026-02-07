plugins {
    alias(libs.plugins.convention.bukkit)
    alias(libs.plugins.resourcefactory.bukkit)
}

dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api:bukkit-api"))

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

val pluginName = "${rootProject.name}-TestPlugin"

tasks.jar {
    archiveBaseName = pluginName
}

bukkitPluginYaml {
    main = "$group.test.NaturalModelsTest"
    version = project.version.toString()
    name = pluginName
    foliaSupported = true
    apiVersion = "1.20"
    author = "toxicity"
    description = "NaturalModels's test plugin"
    depend = listOf(
        "NaturalModels"
    )
    commands.register("rollinfo") {
        usage = "/<command>"
        description = "Gets roll animation's info."
        permission = "NaturalModels.rollinfo"
    }
    commands.register("knightsword") {
        usage = "/<command>"
        description = "Gets knight sword"
        permission = "NaturalModels.knightsword"
    }
}

