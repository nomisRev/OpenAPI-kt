package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    val id: Long,
    val name: String,
    val archived: Boolean,
    val organization: SimpleClassroomOrganization,
    val url: String,
)
