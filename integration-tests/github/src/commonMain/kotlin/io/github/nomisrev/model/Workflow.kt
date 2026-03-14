package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Workflow(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    val path: String,
    val state: State,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("badge_url") val badgeUrl: String,
    @SerialName("deleted_at") val deletedAt: LocalDateTime? = null,
) {
    @Serializable
    enum class State {
        @SerialName("active")
        Active,
        @SerialName("deleted")
        Deleted,
        @SerialName("disabled_fork")
        DisabledFork,
        @SerialName("disabled_inactivity")
        DisabledInactivity,
        @SerialName("disabled_manually")
        DisabledManually;
    }
}
