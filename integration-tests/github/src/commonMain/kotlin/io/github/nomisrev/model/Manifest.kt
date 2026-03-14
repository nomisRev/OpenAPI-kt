package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
data class Manifest(
    val name: String,
    val file: File? = null,
    val metadata: Metadata? = null,
    val resolved: List<Dependency>? = null,
) {
    @Serializable
    @JvmInline
    value class File(@SerialName("source_location") val sourceLocation: String? = null)
}
