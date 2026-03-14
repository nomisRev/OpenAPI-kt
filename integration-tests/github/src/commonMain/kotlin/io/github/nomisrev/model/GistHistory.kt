package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GistHistory(
    val user: NullableSimpleUser? = null,
    val version: String? = null,
    @SerialName("committed_at") val committedAt: LocalDateTime? = null,
    @SerialName("change_status") val changeStatus: ChangeStatus? = null,
    val url: String? = null,
) {
    @Serializable
    data class ChangeStatus(val total: Long? = null, val additions: Long? = null, val deletions: Long? = null)
}
