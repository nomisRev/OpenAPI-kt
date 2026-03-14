package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GistCommit(
    val url: String,
    val version: String,
    val user: NullableSimpleUser?,
    @SerialName("change_status") val changeStatus: ChangeStatus,
    @SerialName("committed_at") val committedAt: LocalDateTime,
) {
    @Serializable
    data class ChangeStatus(val total: Long? = null, val additions: Long? = null, val deletions: Long? = null)
}
