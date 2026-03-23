package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The status of auto merging a pull request.
 */
@Serializable
public data class AutoMerge(
  @SerialName("enabled_by")
  public val enabledBy: SimpleUser,
  @SerialName("merge_method")
  public val mergeMethod: MergeMethod,
  @SerialName("commit_title")
  public val commitTitle: String,
  @SerialName("commit_message")
  public val commitMessage: String,
) {
  @Serializable
  public enum class MergeMethod(
    public val `value`: String,
  ) {
    @SerialName("merge")
    Merge("merge"),
    @SerialName("squash")
    Squash("squash"),
    @SerialName("rebase")
    Rebase("rebase"),
    ;
  }
}
