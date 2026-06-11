package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * An object specifying the format that the model must output.
 *
 * Configuring `{ "type": "json_schema" }` enables Structured Outputs, 
 * which ensures the model will match your supplied JSON schema. Learn more in the 
 * [Structured Outputs guide](/docs/guides/structured-outputs).
 *
 * The default format is `{ "type": "text" }` with no additional options.
 *
 * **Not recommended for gpt-4o and newer models:**
 *
 * Setting to `{ "type": "json_object" }` enables the older JSON mode, which
 * ensures the message the model generates is valid JSON. Using `json_schema`
 * is preferred for models that support it.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface TextResponseFormatConfiguration {
  @Serializable
  @SerialName("text")
  public data object Text : TextResponseFormatConfiguration

  /**
   * JSON Schema response format. Used to generate structured JSON responses.
   * Learn more about [Structured Outputs](/docs/guides/structured-outputs).
   *
   */
  @SerialName("json_schema")
  @Serializable
  public data class JsonSchema(
    public val description: String? = null,
    public val name: String,
    public val schema: ResponseFormatJsonSchemaSchema,
    public val strict: Boolean? = null,
  ) : TextResponseFormatConfiguration

  @Serializable
  @SerialName("json_object")
  public data object JsonObject : TextResponseFormatConfiguration
}
