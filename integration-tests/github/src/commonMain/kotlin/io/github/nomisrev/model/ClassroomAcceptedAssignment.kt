package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ClassroomAcceptedAssignment(
    val id: Long,
    val submitted: Boolean,
    val passing: Boolean,
    @SerialName("commit_count") val commitCount: Long,
    val grade: String,
    val students: List<SimpleClassroomUser>,
    val repository: SimpleClassroomRepository,
    val assignment: SimpleClassroomAssignment,
)
