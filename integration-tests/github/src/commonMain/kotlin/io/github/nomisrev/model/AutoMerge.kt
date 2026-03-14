package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AutoMerge(
    @SerialName("enabled_by") val enabledBy: SimpleUser,
    @SerialName("merge_method") val mergeMethod: MergeMethod,
    @SerialName("commit_title") val commitTitle: String,
    @SerialName("commit_message") val commitMessage: String,
) {
    @Serializable
    enum class MergeMethod {
        @SerialName("merge") Merge, @SerialName("squash") Squash, @SerialName("rebase") Rebase;
    }
}
