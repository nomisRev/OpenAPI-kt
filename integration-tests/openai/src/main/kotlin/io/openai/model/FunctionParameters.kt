package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * The parameters the functions accepts, described as a JSON Schema object. See the [guide](/docs/guides/function-calling) for examples, and the [JSON Schema reference](https://json-schema.org/understanding-json-schema/) for documentation about the format. 
 *
 * Omitting `parameters` defines a function with an empty parameter list.
 */
@JvmInline
@Serializable
public value class FunctionParameters(
  public val `value`: JsonElement,
)
