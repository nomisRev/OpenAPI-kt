package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullableProjectsV2StatusUpdate(
    val id: Double,
    @SerialName("node_id") val nodeId: String,
    @SerialName("project_node_id") val projectNodeId: String? = null,
    val creator: SimpleUser? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val status: Status? = null,
    @SerialName("start_date") val startDate: LocalDate? = null,
    @SerialName("target_date") val targetDate: LocalDate? = null,
    val body: String? = null,
) {
    @Serializable
    enum class Status {
        INACTIVE,
        @SerialName("ON_TRACK")
        ONTRACK,
        @SerialName("AT_RISK")
        ATRISK,
        @SerialName("OFF_TRACK")
        OFFTRACK,
        COMPLETE;
    }
}
