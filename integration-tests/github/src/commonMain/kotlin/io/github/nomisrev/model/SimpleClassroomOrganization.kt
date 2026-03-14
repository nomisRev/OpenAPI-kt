package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SimpleClassroomOrganization(
    val id: Long,
    val login: String,
    @SerialName("node_id") val nodeId: String,
    @SerialName("html_url") val htmlUrl: String,
    val name: String?,
    @SerialName("avatar_url") val avatarUrl: String,
)
