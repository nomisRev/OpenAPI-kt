package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class HookResponse(
  public val code: Long?,
  public val status: String?,
  public val message: String?,
)
