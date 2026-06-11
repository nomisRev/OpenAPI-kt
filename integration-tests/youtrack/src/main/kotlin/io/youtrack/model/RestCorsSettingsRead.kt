package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RestCorsSettingsRead(
  public val id: String? = null,
  public val allowedOrigins: List<String>? = null,
  public val allowAllOrigins: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
