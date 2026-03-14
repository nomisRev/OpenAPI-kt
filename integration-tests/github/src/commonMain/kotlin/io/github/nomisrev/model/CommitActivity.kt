package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class CommitActivity(val days: List<Long>, val total: Long, val week: Long)
