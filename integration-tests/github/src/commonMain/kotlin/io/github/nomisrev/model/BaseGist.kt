package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Required

@Serializable
data class BaseGist(
    val url: String,
    @SerialName("forks_url") val forksUrl: String,
    @SerialName("commits_url") val commitsUrl: String,
    val id: String,
    @SerialName("node_id") val nodeId: String,
    @SerialName("git_pull_url") val gitPullUrl: String,
    @SerialName("git_push_url") val gitPushUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    @Required val files: List<Files>,
    val public: Boolean,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val description: String?,
    val comments: Long,
    @SerialName("comments_enabled") val commentsEnabled: Boolean? = null,
    val user: NullableSimpleUser?,
    @SerialName("comments_url") val commentsUrl: String,
    val owner: SimpleUser? = null,
    val truncated: Boolean? = null,
    val forks: JsonArray? = null,
    val history: JsonArray? = null,
) {
    @Serializable
    data class Files(
        val filename: String? = null,
        val type: String? = null,
        val language: String? = null,
        @SerialName("raw_url") val rawUrl: String? = null,
        val size: Long? = null,
        val encoding: String? = null,
    )
}
