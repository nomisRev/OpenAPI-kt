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
public data class ResponseFormatJsonSchema(
  public val type: Type,
  @SerialName("json_schema")
  public val jsonSchema: JsonSchema,
) {
  /**
   * Structured Outputs configuration options, including a JSON Schema.
   *
   */
  @Serializable
  public data class JsonSchema(
    public val description: String? = null,
    public val name: String,
    public val schema: ResponseFormatJsonSchemaSchema? = null,
    public val strict: Boolean? = null,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("json_schema")
    JsonSchema("json_schema"),
    ;
  }
}
