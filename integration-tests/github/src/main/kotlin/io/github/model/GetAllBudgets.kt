package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GetAllBudgets(
  public val budgets: List<Budget>,
  @SerialName("has_next_page")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasNextPage: Boolean? = null,
  @SerialName("total_count")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val totalCount: Long? = null,
)
