package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PushEvent(
    @SerialName("repository_id") val repositoryId: Long,
    @SerialName("push_id") val pushId: Long,
    val ref: String,
    val head: String,
    val before: String,
)
