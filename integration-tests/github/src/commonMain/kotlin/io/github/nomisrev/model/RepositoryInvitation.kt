package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepositoryInvitation(
    val id: Long,
    val repository: MinimalRepository,
    val invitee: NullableSimpleUser?,
    val inviter: NullableSimpleUser?,
    val permissions: Permissions,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val expired: Boolean? = null,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("node_id") val nodeId: String,
) {
    @Serializable
    enum class Permissions {
        @SerialName("read")
        Read,
        @SerialName("write")
        Write,
        @SerialName("admin")
        Admin,
        @SerialName("triage")
        Triage,
        @SerialName("maintain")
        Maintain;
    }
}
