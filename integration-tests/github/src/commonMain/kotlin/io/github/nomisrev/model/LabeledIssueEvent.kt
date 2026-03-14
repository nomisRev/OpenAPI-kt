package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LabeledIssueEvent(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val actor: SimpleUser,
    val event: String,
    @SerialName("commit_id") val commitId: String?,
    @SerialName("commit_url") val commitUrl: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration?,
    val label: Label,
) {
    @Serializable
    data class Label(val name: String, val color: String)
}
