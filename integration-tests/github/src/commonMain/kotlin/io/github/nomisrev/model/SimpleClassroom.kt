package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class SimpleClassroom(val id: Long, val name: String, val archived: Boolean, val url: String)
