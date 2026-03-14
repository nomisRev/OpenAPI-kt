package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullablePinnedIssueComment(
    @SerialName("pinned_at") val pinnedAt: LocalDateTime,
    @SerialName("pinned_by") val pinnedBy: NullableSimpleUser?,
)
