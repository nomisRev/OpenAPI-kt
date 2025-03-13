package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class RequestBody(
  /**
   * A brief description of the request body. This could contain examples of use. CommonMark syntax
   * MAY be used for rich text representation.
   */
  public val description: String? = null,
  /**
   * The content of the request body. The key is a media type or media type range and the value
   * describes it. For requests that match multiple keys, only the most specific key is applicable.
   * e.g. text/plain overrides text
   */
  public val content: Map<String, MediaType>,
  /** Determines if the request body is required in the request. Defaults to false. */
  public val required: Boolean = false,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
)
