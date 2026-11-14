package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Request payload for creating a new group in the organization.
 */
@Serializable
public data class CreateGroupBody(
  public val name: String,
)
