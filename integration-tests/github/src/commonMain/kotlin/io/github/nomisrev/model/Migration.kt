package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Migration(
    val id: Long,
    val owner: NullableSimpleUser?,
    val guid: String,
    val state: String,
    @SerialName("lock_repositories") val lockRepositories: Boolean,
    @SerialName("exclude_metadata") val excludeMetadata: Boolean,
    @SerialName("exclude_git_data") val excludeGitData: Boolean,
    @SerialName("exclude_attachments") val excludeAttachments: Boolean,
    @SerialName("exclude_releases") val excludeReleases: Boolean,
    @SerialName("exclude_owner_projects") val excludeOwnerProjects: Boolean,
    @SerialName("org_metadata_only") val orgMetadataOnly: Boolean,
    val repositories: List<Repository>,
    val url: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("node_id") val nodeId: String,
    @SerialName("archive_url") val archiveUrl: String? = null,
    val exclude: List<String>? = null,
)
