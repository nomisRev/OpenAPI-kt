package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueLinkResponse(
    val id: String? = null,
    val direction: Direction? = null,
    val linkType: IssueLinkTypeResponse? = null,
    val issues: List<IssueResponse>? = null,
    val trimmedIssues: List<IssueResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
) {
    @Serializable
    enum class Direction {
        OUTWARD, INWARD, BOTH;
    }
}
