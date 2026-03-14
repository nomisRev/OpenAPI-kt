package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Release(
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("assets_url") val assetsUrl: String,
    @SerialName("upload_url") val uploadUrl: String,
    @SerialName("tarball_url") val tarballUrl: String?,
    @SerialName("zipball_url") val zipballUrl: String?,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("tag_name") val tagName: String,
    @SerialName("target_commitish") val targetCommitish: String,
    val name: String?,
    val body: String? = null,
    val draft: Boolean,
    val prerelease: Boolean,
    val immutable: Boolean? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("published_at") val publishedAt: LocalDateTime?,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    val author: SimpleUser,
    val assets: List<ReleaseAsset>,
    @SerialName("body_html") val bodyHtml: String? = null,
    @SerialName("body_text") val bodyText: String? = null,
    @SerialName("mentions_count") val mentionsCount: Long? = null,
    @SerialName("discussion_url") val discussionUrl: String? = null,
    val reactions: ReactionRollup? = null,
)
