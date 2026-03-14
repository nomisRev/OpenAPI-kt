package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullableActionsHostedRunnerPoolImage(
    val id: String,
    @SerialName("size_gb") val sizeGb: Long,
    @SerialName("display_name") val displayName: String,
    val source: Source,
    val version: String? = null,
) {
    @Serializable
    enum class Source {
        @SerialName("github") Github, @SerialName("partner") Partner, @SerialName("custom") Custom;
    }
}
