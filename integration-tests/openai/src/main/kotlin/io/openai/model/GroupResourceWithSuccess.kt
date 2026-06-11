package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response returned after updating a group.
 */
@Serializable
public data class GroupResourceWithSuccess(
  public val id: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("is_scim_managed")
  public val isScimManaged: Boolean,
)
