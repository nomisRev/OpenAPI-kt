plugins {
    id(libs.plugins.jvm.get().pluginId)
    application
    alias(libs.plugins.testballoon)
}

val corpusCompiler = configurations.create("corpusCompiler")
val corpusSerialization = configurations.create("corpusSerialization")

dependencies {
    implementation(projects.renderer)
    implementation(libs.json)
    implementation(libs.coroutines.core)
    implementation(libs.datetime)
    implementation(ktorLibs.client.core)
    implementation(ktorLibs.client.cio)
    implementation(ktorLibs.client.contentNegotiation)
    implementation(ktorLibs.serialization.kotlinx.json)
    testImplementation(libs.test)
    testImplementation(libs.testballoon)
    testImplementation(ktorLibs.client.mock)
    corpusCompiler("org.jetbrains.kotlin:kotlin-compiler-embeddable:${libs.versions.kotlin.get()}")
    corpusSerialization("org.jetbrains.kotlin:kotlin-serialization-compiler-plugin-embeddable:${libs.versions.kotlin.get()}")
}

tasks.register("prepareCorpus") {
    group = "verification"
    description = "Build the isolated API Guru exploration worker and export compiler classpaths."
    dependsOn(tasks.named("classes"))
    doLast {
        val destination = layout.buildDirectory.dir("runner").get().asFile
        destination.mkdirs()
        destination.resolve("runtime.txt").writeText(sourceSets.main.get().runtimeClasspath.asPath)
        destination.resolve("compiler.txt").writeText(corpusCompiler.asPath)
        destination.resolve("serialization.txt").writeText(corpusSerialization.singleFile.absolutePath)
    }
}

application {
    mainClass.set("exploration.CorpusKt")
}

tasks.named<JavaExec>("run") {
    dependsOn("prepareCorpus")
    workingDir(rootProject.projectDir)
}
