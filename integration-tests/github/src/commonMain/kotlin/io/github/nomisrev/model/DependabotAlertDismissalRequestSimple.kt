package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DependabotAlertDismissalRequestSimple(
    val id: Long? = null,
    val status: Status? = null,
    val requester: Requester? = null,
    @SerialName("created_at") val createdAt: LocalDateTime? = null,
    val url: String? = null,
) {
    @Serializable
    enum class Status {
        @SerialName("pending")
        Pending,
        @SerialName("approved")
        Approved,
        @SerialName("rejected")
        Rejected,
        @SerialName("cancelled")
        Cancelled;
    }

    @Serializable
    data class Requester(val id: Long? = null, val login: String? = null)
}
