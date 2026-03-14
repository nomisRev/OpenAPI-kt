package io.github.nomisrev.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectsV2IterationSettings(
    val id: String,
    @SerialName("start_date") val startDate: LocalDate,
    val duration: Long,
    val title: Title,
    val completed: Boolean,
) {
    @Serializable
    data class Title(val raw: String, val html: String)
}
