package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RestCorsSettingsWrite(
  public val allowedOrigins: List<String>? = null,
  public val allowAllOrigins: Boolean? = null,
)
