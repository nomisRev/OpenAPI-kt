package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Short Branch
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ShortBranch(
  public val name: String,
  public val commit: Commit,
  public val `protected`: Boolean,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val protection: BranchProtection? = null,
  @SerialName("protection_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val protectionUrl: String? = null,
) {
  @Serializable
  public data class Commit(
    public val sha: String,
    public val url: String,
  )
}
