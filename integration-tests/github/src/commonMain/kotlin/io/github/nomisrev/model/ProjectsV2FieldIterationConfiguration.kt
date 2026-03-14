package io.github.nomisrev.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectsV2FieldIterationConfiguration(
    @SerialName("start_date") val startDate: LocalDate? = null,
    val duration: Long? = null,
    val iterations: List<Iterations>? = null,
) {
    @Serializable
    data class Iterations(
        val title: String? = null,
        @SerialName("start_date") val startDate: LocalDate? = null,
        val duration: Long? = null,
    )
}
