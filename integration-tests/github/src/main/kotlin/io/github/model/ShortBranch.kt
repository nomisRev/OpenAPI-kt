package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Short Branch
 */
@Serializable
public data class ShortBranch(
  public val name: String,
  public val commit: Commit,
  public val `protected`: Boolean,
  public val protection: BranchProtection? = null,
  @SerialName("protection_url")
  public val protectionUrl: String? = null,
) {
  @Serializable
  public data class Commit(
    public val sha: String,
    public val url: String,
  )
}
