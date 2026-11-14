package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for updating an existing role.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PublicUpdateOrganizationRoleBody(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val permissions: Optional<List<String>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
  @SerialName("role_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val roleName: Optional<String> = Optional.Absent,
)
