package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * For an article that was imported from another service, this entity represents the reference to the article in the external system.
 */
@Serializable
public data class ExternalArticle(
  public val id: String? = null,
  public val name: String? = null,
  public val url: String? = null,
  public val key: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
