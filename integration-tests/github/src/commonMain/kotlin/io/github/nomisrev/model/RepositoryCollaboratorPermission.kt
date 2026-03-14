package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepositoryCollaboratorPermission(
    val permission: String,
    @SerialName("role_name") val roleName: String,
    val user: NullableCollaborator?,
)
