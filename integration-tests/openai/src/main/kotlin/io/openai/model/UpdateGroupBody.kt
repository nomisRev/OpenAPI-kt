package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Request payload for updating the details of an existing group.
 */
@Serializable
public data class UpdateGroupBody(
  public val name: String,
)
