package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Thread(
    val id: String,
    val repository: MinimalRepository,
    val subject: Subject,
    val reason: String,
    val unread: Boolean,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("last_read_at") val lastReadAt: String?,
    val url: String,
    @SerialName("subscription_url") val subscriptionUrl: String,
) {
    @Serializable
    data class Subject(
        val title: String,
        val url: String,
        @SerialName("latest_comment_url") val latestCommentUrl: String,
        val type: String,
    )
}
