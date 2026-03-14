package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class StarredRepository(@SerialName("starred_at") val starredAt: LocalDateTime, val repo: Repository)
