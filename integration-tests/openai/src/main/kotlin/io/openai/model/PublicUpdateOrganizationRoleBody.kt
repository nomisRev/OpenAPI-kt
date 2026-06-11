package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for updating an existing role.
 */
@Serializable
public data class PublicUpdateOrganizationRoleBody(
  public val permissions: List<String>? = null,
  public val description: String? = null,
  @SerialName("role_name")
  public val roleName: String? = null,
)
