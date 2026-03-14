package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ThreadSubscription(
    val subscribed: Boolean,
    val ignored: Boolean,
    val reason: String?,
    @SerialName("created_at") val createdAt: LocalDateTime?,
    val url: String,
    @SerialName("thread_url") val threadUrl: String? = null,
    @SerialName("repository_url") val repositoryUrl: String? = null,
)
