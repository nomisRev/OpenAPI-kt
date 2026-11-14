package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Branch With Protection
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BranchWithProtection(
  public val name: String,
  public val commit: Commit,
  @SerialName("_links")
  public val links: Links,
  public val `protected`: Boolean,
  public val protection: BranchProtection,
  @SerialName("protection_url")
  public val protectionUrl: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pattern: String? = null,
  @SerialName("required_approving_review_count")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredApprovingReviewCount: Long? = null,
) {
  @Serializable
  public data class Links(
    public val html: String,
    public val self: String,
  )
}
