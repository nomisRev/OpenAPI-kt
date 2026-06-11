package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GetAllBudgets(
  public val budgets: List<Budget>,
  @SerialName("has_next_page")
  public val hasNextPage: Boolean? = null,
  @SerialName("total_count")
  public val totalCount: Long? = null,
)
