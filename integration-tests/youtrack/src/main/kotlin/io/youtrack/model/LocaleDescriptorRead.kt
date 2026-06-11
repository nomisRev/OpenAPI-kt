package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a language locale that is used in UI.
 */
@Serializable
public data class LocaleDescriptorRead(
  public val id: String? = null,
  public val locale: String? = null,
  public val language: String? = null,
  public val community: Boolean? = null,
  public val name: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
