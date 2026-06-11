package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteBudget(
  public val message: String,
  public val id: String,
)
