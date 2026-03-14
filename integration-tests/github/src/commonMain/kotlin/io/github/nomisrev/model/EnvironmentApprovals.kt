package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName

@Serializable
data class EnvironmentApprovals(
    val environments: List<Environments>,
    val state: State,
    val user: SimpleUser,
    val comment: String,
) {
    @Serializable
    data class Environments(
        val id: Long? = null,
        @SerialName("node_id") val nodeId: String? = null,
        val name: String? = null,
        val url: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        @SerialName("created_at") val createdAt: LocalDateTime? = null,
        @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    )

    @Serializable
    enum class State {
        @SerialName("approved") Approved, @SerialName("rejected") Rejected, @SerialName("pending") Pending;
    }
}
