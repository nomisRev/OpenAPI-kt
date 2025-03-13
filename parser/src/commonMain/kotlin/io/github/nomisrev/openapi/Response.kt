package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class Response(
  /**
   * A short description of the response. CommonMark's syntax MAY be used for rich text
   * representation.
   */
  public val description: String? = null,
  /** Maps a header name to its definition. RFC7230 states header names are case-insensitive. */
  public val headers: Map<String, ReferenceOr<Header>> = emptyMap(),
  /**
   * A map containing descriptions of potential response payloads. The key is a media type or media
   * type range and the value describes it. For responses that match multiple keys, only the most
   * specific key is applicable. i.e. text/plain overrides text
   */
  public val content: Map<String, MediaType> = emptyMap(),
  /**
   * A map of operations links that can be followed from the response. The key of the map is a short
   * name for the link, following the naming constraints of the names for Component Objects.
   */
  public val links: Map<String, ReferenceOr<Link>> = emptyMap(),
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
) {
  public operator fun plus(other: Response): Response =
    Response(
      description,
      headers + other.headers,
      content + other.content,
      links + other.links,
      extensions + other.extensions,
    )
}
