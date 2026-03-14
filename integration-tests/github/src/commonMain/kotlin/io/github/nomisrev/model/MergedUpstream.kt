package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MergedUpstream(
    val message: String? = null,
    @SerialName("merge_type") val mergeType: MergeType? = null,
    @SerialName("base_branch") val baseBranch: String? = null,
) {
    @Serializable
    enum class MergeType {
        @SerialName("merge") Merge, @SerialName("fast-forward") FastForward, @SerialName("none") None;
    }
}
