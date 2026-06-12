plugins {
    id(libs.plugins.jvm.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gratatouille)
    id(libs.plugins.publish.get().pluginId)
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            from(components["java"])
        }
    }
}

// Vanniktech creates a `maven` publication from the same component as `pluginMaven`,
// so both signing tasks produce .asc files for the same jars. Gradle 9.5+ flags
// publishMavenPublicationToMavenCentralRepository for consuming outputs of
// signPluginMavenPublication without declaring a dependency. mustRunAfter makes
// the ordering explicit across all sign/publish task pairs.
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(tasks.withType<Sign>())
}

gratatouille {
    pluginMarker("io.github.nomisrev.openapi")
}

dependencies {
    compileOnly(gradleApi())
    implementation(libs.gratatouille.runtime)
    compileOnly(libs.gradle)
    implementation(project(":renderer"))
    implementation(libs.coroutines.core)
}

configurations.matching { it.name == "gratatouille" }.configureEach {
    attributes {
        attribute(
            Usage.USAGE_ATTRIBUTE,
            objects.named(Usage::class.java, Usage.JAVA_RUNTIME)
        )
    }
}
