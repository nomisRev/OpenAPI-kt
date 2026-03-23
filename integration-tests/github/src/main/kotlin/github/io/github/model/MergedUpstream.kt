package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Results of a successful merge upstream request
 */
@Serializable
public data class MergedUpstream(
  public val message: String? = null,
  @SerialName("merge_type")
  public val mergeType: MergeType? = null,
  @SerialName("base_branch")
  public val baseBranch: String? = null,
) {
  @Serializable
  public enum class MergeType(
    public val `value`: String,
  ) {
    @SerialName("merge")
    Merge("merge"),
    @SerialName("fast-forward")
    FastForward("fast-forward"),
    @SerialName("none")
    None("none"),
    ;
  }
}
