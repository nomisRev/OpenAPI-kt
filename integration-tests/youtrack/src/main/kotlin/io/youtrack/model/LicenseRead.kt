package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LicenseRead(
  public val id: String? = null,
  public val username: String? = null,
  public val license: String? = null,
  public val error: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
