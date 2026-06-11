package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Request payload for creating a new group in the organization.
 */
@JvmInline
@Serializable
public value class CreateGroupBody(
  public val name: String,
)
