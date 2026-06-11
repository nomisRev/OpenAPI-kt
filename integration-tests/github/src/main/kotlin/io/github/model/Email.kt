package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Email
 */
@Serializable
public data class Email(
  public val email: String,
  public val primary: Boolean,
  public val verified: Boolean,
  public val visibility: String?,
)
