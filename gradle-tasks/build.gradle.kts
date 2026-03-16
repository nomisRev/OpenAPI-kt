plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gratatouille.tasks)
}

dependencies {
    implementation(libs.gratatouille.tasks.runtime)
    implementation(project(mapOf("path" to ":renderer", "configuration" to "jvmRuntimeElements")))
    implementation(libs.coroutines.core)
}
