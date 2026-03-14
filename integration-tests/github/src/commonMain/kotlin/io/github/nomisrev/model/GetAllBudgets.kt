package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GetAllBudgets(
    val budgets: List<Budget>,
    @SerialName("has_next_page") val hasNextPage: Boolean? = null,
    @SerialName("total_count") val totalCount: Long? = null,
)
