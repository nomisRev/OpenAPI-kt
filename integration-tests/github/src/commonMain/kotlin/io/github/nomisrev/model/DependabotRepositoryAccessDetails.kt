package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DependabotRepositoryAccessDetails(
    @SerialName("default_level") val defaultLevel: DefaultLevel? = null,
    @SerialName("accessible_repositories") val accessibleRepositories: List<NullableSimpleRepository>? = null,
) {
    @Serializable
    enum class DefaultLevel {
        @SerialName("public") Public, @SerialName("internal") Internal;
    }
}
