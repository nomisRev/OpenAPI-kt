package io.github.nomisrev.openapi

data class RenderConfig(
    val modelPackage: String = "io.github.nomisrev.openapi.generated.model",
    val apiPackage: String = "io.github.nomisrev.openapi.generated.api",
    val targets: Set<KmpTarget> = setOf(KmpTarget.JVM, KmpTarget.JS),
    val contextSpecificNames: Set<String> = emptySet(),
)

enum class KmpTarget {
    JVM,
    JS,
}
