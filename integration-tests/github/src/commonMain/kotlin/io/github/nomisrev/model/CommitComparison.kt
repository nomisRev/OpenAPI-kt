package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CommitComparison(
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("permalink_url") val permalinkUrl: String,
    @SerialName("diff_url") val diffUrl: String,
    @SerialName("patch_url") val patchUrl: String,
    @SerialName("base_commit") val baseCommit: Commit,
    @SerialName("merge_base_commit") val mergeBaseCommit: Commit,
    val status: Status,
    @SerialName("ahead_by") val aheadBy: Long,
    @SerialName("behind_by") val behindBy: Long,
    @SerialName("total_commits") val totalCommits: Long,
    val commits: List<Commit>,
    val files: List<DiffEntry>? = null,
) {
    @Serializable
    enum class Status {
        @SerialName("diverged")
        Diverged,
        @SerialName("ahead")
        Ahead,
        @SerialName("behind")
        Behind,
        @SerialName("identical")
        Identical;
    }
}
