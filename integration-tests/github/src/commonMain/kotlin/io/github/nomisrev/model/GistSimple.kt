package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.Required

@Serializable
data class GistSimple(
    val forks: List<Forks>? = null,
    val history: List<GistHistory>? = null,
    @SerialName("fork_of") val forkOf: ForkOf? = null,
    val url: String? = null,
    @SerialName("forks_url") val forksUrl: String? = null,
    @SerialName("commits_url") val commitsUrl: String? = null,
    val id: String? = null,
    @SerialName("node_id") val nodeId: String? = null,
    @SerialName("git_pull_url") val gitPullUrl: String? = null,
    @SerialName("git_push_url") val gitPushUrl: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
    val files: List<Files>? = null,
    val public: Boolean? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    val description: String? = null,
    val comments: Long? = null,
    @SerialName("comments_enabled") val commentsEnabled: Boolean? = null,
    val user: String? = null,
    @SerialName("comments_url") val commentsUrl: String? = null,
    val owner: SimpleUser? = null,
    val truncated: Boolean? = null,
) {
    @Serializable
    data class Forks(
        val id: String? = null,
        val url: String? = null,
        val user: PublicUser? = null,
        @SerialName("created_at") val createdAt: LocalDateTime? = null,
        @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    )

    @Serializable
    data class ForkOf(
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
        val owner: NullableSimpleUser? = null,
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
        )
    }

    @Serializable
    data class Files(
        val filename: String? = null,
        val type: String? = null,
        val language: String? = null,
        @SerialName("raw_url") val rawUrl: String? = null,
        val size: Long? = null,
        val truncated: Boolean? = null,
        val content: String? = null,
        val encoding: String? = null,
    )
}
