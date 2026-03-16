plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gratatouille)
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            from(components["java"])
        }
    }
}

gratatouille {
    pluginMarker("io.github.nomisrev.openapi")
}

dependencies {
    compileOnly(gradleApi())
    implementation(libs.gratatouille.runtime)
    gratatouille(projects.gradleTasks)
    compileOnly(libs.gradle)
}

configurations.matching { it.name == "gratatouille" }.configureEach {
    attributes {
        attribute(
            Usage.USAGE_ATTRIBUTE,
            objects.named(Usage::class.java, Usage.JAVA_RUNTIME)
        )
    }
}
