package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for creating a custom role.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PublicCreateOrganizationRoleBody(
  @SerialName("role_name")
  public val roleName: String,
  public val permissions: List<String>,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
)
