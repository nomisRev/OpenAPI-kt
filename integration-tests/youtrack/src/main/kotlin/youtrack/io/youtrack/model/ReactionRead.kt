package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReactionRead(
  public val id: String? = null,
  public val author: UserRead? = null,
  public val reaction: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
