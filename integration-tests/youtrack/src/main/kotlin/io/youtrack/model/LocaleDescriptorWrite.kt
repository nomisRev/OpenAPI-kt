package io.youtrack.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a language locale that is used in UI.
 */
@Serializable
public data class LocaleDescriptorWrite(
  public val name: String? = null,
)
