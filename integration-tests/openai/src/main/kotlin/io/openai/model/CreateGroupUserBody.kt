package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for adding a user to a group.
 */
@Serializable
public data class CreateGroupUserBody(
  @SerialName("user_id")
  public val userId: String,
)
