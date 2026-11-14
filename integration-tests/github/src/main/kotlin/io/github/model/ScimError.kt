package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Scim Error
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ScimError(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: Optional<String> = Optional.Absent,
  @SerialName("documentation_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val documentationUrl: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val detail: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val scimType: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val schemas: List<String>? = null,
)
