package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Provides details of Public IP for a GitHub-hosted larger runners
 */
@Serializable
public data class PublicIp(
  public val enabled: Boolean? = null,
  public val prefix: String? = null,
  public val length: Long? = null,
)
