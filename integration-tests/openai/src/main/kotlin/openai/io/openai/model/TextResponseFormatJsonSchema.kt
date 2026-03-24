package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * JSON Schema response format. Used to generate structured JSON responses.
 * Learn more about [Structured Outputs](/docs/guides/structured-outputs).
 *
 */
@Serializable
public data class TextResponseFormatJsonSchema(
  public val type: Type,
  public val description: String? = null,
  public val name: String,
  public val schema: ResponseFormatJsonSchemaSchema,
  public val strict: Boolean? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("json_schema")
    JsonSchema("json_schema"),
    ;
  }
}
