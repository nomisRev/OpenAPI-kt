package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepositorySubscription(
    val subscribed: Boolean,
    val ignored: Boolean,
    val reason: String?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val url: String,
    @SerialName("repository_url") val repositoryUrl: String,
)
