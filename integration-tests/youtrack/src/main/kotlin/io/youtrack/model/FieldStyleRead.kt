package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the style settings of the field in YouTrack.
 */
@Serializable
public data class FieldStyleRead(
  public val id: String? = null,
  public val background: String? = null,
  public val foreground: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
