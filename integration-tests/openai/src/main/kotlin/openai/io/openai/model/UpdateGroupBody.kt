package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Request payload for updating the details of an existing group.
 */
@JvmInline
@Serializable
public value class UpdateGroupBody(
  public val name: String,
)
