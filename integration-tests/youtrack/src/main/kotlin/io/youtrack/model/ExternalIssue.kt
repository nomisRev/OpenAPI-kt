package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * For an issue that was imported from another service, represents the reference to the issue in the external system.
 */
@Serializable
public data class ExternalIssue(
  public val id: String? = null,
  public val name: String? = null,
  public val url: String? = null,
  public val key: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
