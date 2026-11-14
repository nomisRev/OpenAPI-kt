package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class JsonSchema(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val description: String? = null,
    public val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val schema: ResponseFormatJsonSchemaSchema? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val strict: Optional<Boolean> = Optional.Absent,
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
