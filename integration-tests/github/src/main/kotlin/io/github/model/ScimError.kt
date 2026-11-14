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
  public val message: String? = null,
  @SerialName("documentation_url")
  public val documentationUrl: String? = null,
  public val detail: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: Long? = null,
  public val scimType: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val schemas: List<String>? = null,
)
