package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Branch Short
 */
@Serializable
public data class BranchShort(
  public val name: String,
  public val commit: Commit,
  public val `protected`: Boolean,
) {
  @Serializable
  public data class Commit(
    public val sha: String,
    public val url: String,
  )
}
