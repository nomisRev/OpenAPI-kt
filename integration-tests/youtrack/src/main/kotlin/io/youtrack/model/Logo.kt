package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Company logo that is used in YouTrack.
 */
@Serializable
public data class Logo(
  public val id: String? = null,
  public val url: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
