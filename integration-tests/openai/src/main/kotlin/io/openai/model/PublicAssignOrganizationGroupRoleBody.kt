package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for assigning a role to a group or user.
 */
@Serializable
public data class PublicAssignOrganizationGroupRoleBody(
  @SerialName("role_id")
  public val roleId: String,
)
