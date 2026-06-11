package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repository Collaborator Permission
 */
@Serializable
public data class RepositoryCollaboratorPermission(
  public val permission: String,
  @SerialName("role_name")
  public val roleName: String,
  public val user: NullableCollaborator?,
)
