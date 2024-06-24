package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class Example(
  /** Short description for the example. */
  public val summary: String? = null,
  /**
   * Long description for the example. CommonMark syntax MAY be used for rich text representation.
   */
  public val description: String? = null,
  /**
   * Embedded literal example. The value field and externalValue field are mutually exclusive. To
   * represent examples of media types that cannot naturally represented in JSON or YAML, use a
   * string value to contain the example, escaping where necessary.
   */
  public val value: ExampleValue? = null,
  /**
   * A URL that points to the literal example. This provides the capability to reference examples
   * that cannot easily be included in JSON or YAML documents. The value field and externalValue
   * field are mutually exclusive.
   */
  public val externalValue: String? = null,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap()
) {
  public companion object {
    internal object Serializer :
      KSerializerWithExtensions<Example>(
        serializer(),
        Example::extensions,
        { op, extensions -> op.copy(extensions = extensions) }
      )
  }
}
