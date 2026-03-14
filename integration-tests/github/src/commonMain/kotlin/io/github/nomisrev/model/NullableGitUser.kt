package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NullableGitUser(val name: String? = null, val email: String? = null, val date: LocalDateTime? = null)
