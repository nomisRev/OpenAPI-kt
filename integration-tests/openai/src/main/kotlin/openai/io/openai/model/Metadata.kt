package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Set of 16 key-value pairs that can be attached to an object. This can be
 * useful for storing additional information about the object in a structured
 * format, and querying for objects via API or the dashboard.
 *
 * Keys are strings with a maximum length of 64 characters. Values are strings
 * with a maximum length of 512 characters.
 *
 */
@JvmInline
@Serializable
public value class Metadata(
  public val values: List<String>? = null,
)
