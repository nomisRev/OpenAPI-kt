package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsHostedRunnerCustomImage(
    val id: Long,
    val platform: String,
    @SerialName("total_versions_size") val totalVersionsSize: Long,
    val name: String,
    val source: String,
    @SerialName("versions_count") val versionsCount: Long,
    @SerialName("latest_version") val latestVersion: String,
    val state: String,
)
