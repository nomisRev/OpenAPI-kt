package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Dependency(
    @SerialName("package_url") val packageUrl: String? = null,
    val metadata: Metadata? = null,
    val relationship: Relationship? = null,
    val scope: Scope? = null,
    val dependencies: List<String>? = null,
) {
    @Serializable
    enum class Relationship {
        @SerialName("direct") Direct, @SerialName("indirect") Indirect;
    }

    @Serializable
    enum class Scope {
        @SerialName("runtime") Runtime, @SerialName("development") Development;
    }
}
