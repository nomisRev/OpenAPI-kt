package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Artifact(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    @SerialName("size_in_bytes") val sizeInBytes: Long,
    val url: String,
    @SerialName("archive_download_url") val archiveDownloadUrl: String,
    val expired: Boolean,
    @SerialName("created_at") val createdAt: LocalDateTime?,
    @SerialName("expires_at") val expiresAt: LocalDateTime?,
    @SerialName("updated_at") val updatedAt: LocalDateTime?,
    val digest: String? = null,
    @SerialName("workflow_run") val workflowRun: WorkflowRun? = null,
) {
    @Serializable
    data class WorkflowRun(
        val id: Long? = null,
        @SerialName("repository_id") val repositoryId: Long? = null,
        @SerialName("head_repository_id") val headRepositoryId: Long? = null,
        @SerialName("head_branch") val headBranch: String? = null,
        @SerialName("head_sha") val headSha: String? = null,
    )
}
