package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectColorResponse(
    val id: String? = null,
    val project: Project? = null,
    val color: FieldStyleResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
