package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Scim Error
 */
@Serializable
public data class ScimError(
  public val message: String? = null,
  @SerialName("documentation_url")
  public val documentationUrl: String? = null,
  public val detail: String? = null,
  public val status: Long? = null,
  public val scimType: String? = null,
  public val schemas: List<String>? = null,
)
