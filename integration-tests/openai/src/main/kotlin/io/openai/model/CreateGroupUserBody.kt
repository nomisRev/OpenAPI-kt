package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for adding a user to a group.
 */
@JvmInline
@Serializable
public value class CreateGroupUserBody(
  @SerialName("user_id")
  public val userId: String,
)
