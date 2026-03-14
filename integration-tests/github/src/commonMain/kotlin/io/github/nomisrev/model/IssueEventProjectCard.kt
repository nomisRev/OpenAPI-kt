package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueEventProjectCard(
    val url: String,
    val id: Long,
    @SerialName("project_url") val projectUrl: String,
    @SerialName("project_id") val projectId: Long,
    @SerialName("column_name") val columnName: String,
    @SerialName("previous_column_name") val previousColumnName: String? = null,
)
