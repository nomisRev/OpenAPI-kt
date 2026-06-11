package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for assigning a role to a group or user.
 */
@JvmInline
@Serializable
public value class PublicAssignOrganizationGroupRoleBody(
  @SerialName("role_id")
  public val roleId: String,
)
