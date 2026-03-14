package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SimpleClassroomRepository(
    val id: Long,
    @SerialName("full_name") val fullName: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("node_id") val nodeId: String,
    val private: Boolean,
    @SerialName("default_branch") val defaultBranch: String,
)
