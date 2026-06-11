package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * The schema for the response format, described as a JSON Schema object.
 * Learn how to build JSON schemas [here](https://json-schema.org/).
 *
 */
@JvmInline
@Serializable
public value class ResponseFormatJsonSchemaSchema(
  public val `value`: JsonElement,
)
