package io.github.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Check Dependabot security updates
 */
@Serializable
public data class CheckAutomatedSecurityFixes(
  public val enabled: Boolean,
  public val paused: Boolean,
)
