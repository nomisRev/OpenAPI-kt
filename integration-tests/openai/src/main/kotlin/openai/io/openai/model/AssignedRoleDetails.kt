package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Detailed information about a role assignment entry returned when listing assignments.
 */
@Serializable
public data class AssignedRoleDetails(
  public val id: String,
  public val name: String,
  public val permissions: List<String>,
  @SerialName("resource_type")
  public val resourceType: String,
  @SerialName("predefined_role")
  public val predefinedRole: Boolean,
  public val description: String?,
  @SerialName("created_at")
  public val createdAt: Long?,
  @SerialName("updated_at")
  public val updatedAt: Long?,
  @SerialName("created_by")
  public val createdBy: String?,
  @SerialName("created_by_user_obj")
  public val createdByUserObj: JsonElement?,
  public val metadata: JsonElement?,
)
