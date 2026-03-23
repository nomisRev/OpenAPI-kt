package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A workflow referenced/reused by the initial caller workflow
 */
@Serializable
public data class ReferencedWorkflow(
  public val path: String,
  public val sha: String,
  public val ref: String? = null,
)
