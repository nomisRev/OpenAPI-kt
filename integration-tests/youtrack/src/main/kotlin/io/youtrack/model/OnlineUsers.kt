package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stores number of online user.
 */
@Serializable
public data class OnlineUsers(
  public val id: String? = null,
  public val users: Int? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
