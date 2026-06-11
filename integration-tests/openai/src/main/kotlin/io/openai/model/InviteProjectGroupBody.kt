package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request payload for granting a group access to a project.
 */
@Serializable
public data class InviteProjectGroupBody(
  @SerialName("group_id")
  public val groupId: String,
  public val role: String,
)
