package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * JSON object response format. An older method of generating JSON responses.
 * Using `json_schema` is recommended for models that support it. Note that the
 * model will not generate JSON without a system or user message instructing it
 * to do so.
 *
 */
@JvmInline
@Serializable
public value class ResponseFormatJsonObject(
  public val type: Type,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("json_object")
    JsonObject("json_object"),
    ;
  }
}
