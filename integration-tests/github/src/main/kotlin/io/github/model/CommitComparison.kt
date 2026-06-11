package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commit Comparison
 */
@Serializable
public data class CommitComparison(
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("permalink_url")
  public val permalinkUrl: String,
  @SerialName("diff_url")
  public val diffUrl: String,
  @SerialName("patch_url")
  public val patchUrl: String,
  @SerialName("base_commit")
  public val baseCommit: Commit,
  @SerialName("merge_base_commit")
  public val mergeBaseCommit: Commit,
  public val status: Status,
  @SerialName("ahead_by")
  public val aheadBy: Long,
  @SerialName("behind_by")
  public val behindBy: Long,
  @SerialName("total_commits")
  public val totalCommits: Long,
  public val commits: List<Commit>,
  public val files: List<DiffEntry>? = null,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("diverged")
    Diverged("diverged"),
    @SerialName("ahead")
    Ahead("ahead"),
    @SerialName("behind")
    Behind("behind"),
    @SerialName("identical")
    Identical("identical"),
    ;
  }
}
