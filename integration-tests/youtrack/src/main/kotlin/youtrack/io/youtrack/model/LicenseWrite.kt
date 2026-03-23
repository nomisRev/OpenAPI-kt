package io.youtrack.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class LicenseWrite(
  public val username: String? = null,
  public val license: String? = null,
)
