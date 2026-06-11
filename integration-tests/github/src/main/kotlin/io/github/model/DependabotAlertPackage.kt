package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Details for the vulnerable package.
 */
@Serializable
public data class DependabotAlertPackage(
  public val ecosystem: String,
  public val name: String,
)
