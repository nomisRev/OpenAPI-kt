package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for creating a custom role.
 */
@Serializable
public data class PublicCreateOrganizationRoleBody(
  @SerialName("role_name")
  public val roleName: String,
  public val permissions: List<String>,
  public val description: String? = null,
)
