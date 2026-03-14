package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectsV2(
    val id: Double,
    @SerialName("node_id") val nodeId: String,
    val owner: SimpleUser,
    val creator: SimpleUser,
    val title: String,
    val description: String?,
    val public: Boolean,
    @SerialName("closed_at") val closedAt: LocalDateTime?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val number: Long,
    @SerialName("short_description") val shortDescription: String?,
    @SerialName("deleted_at") val deletedAt: LocalDateTime?,
    @SerialName("deleted_by") val deletedBy: NullableSimpleUser?,
    val state: State? = null,
    @SerialName("latest_status_update") val latestStatusUpdate: NullableProjectsV2StatusUpdate? = null,
    @SerialName("is_template") val isTemplate: Boolean? = null,
) {
    @Serializable
    enum class State {
        @SerialName("open") Open, @SerialName("closed") Closed;
    }
}
