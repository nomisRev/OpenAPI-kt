package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationSimple(
    val login: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    @SerialName("repos_url") val reposUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("hooks_url") val hooksUrl: String,
    @SerialName("issues_url") val issuesUrl: String,
    @SerialName("members_url") val membersUrl: String,
    @SerialName("public_members_url") val publicMembersUrl: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val description: String?,
)
