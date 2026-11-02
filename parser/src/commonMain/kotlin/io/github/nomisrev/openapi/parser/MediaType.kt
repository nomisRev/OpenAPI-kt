package io.github.nomisrev.openapi.parser

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** Each Media Type Object provides schema and examples for the media type identified by its key. */
@Serializable(MediaType.Companion.Serializer::class)
@OptIn(ExperimentalSerializationApi::class)
@Suppress("RUNTIME_ANNOTATION_NOT_SUPPORTED")
@KeepGeneratedSerializer
public data class MediaType(
  /** The schema defining the content of the request, response, or parameter. */
  public val schema: ReferenceOr<Schema>? = null,
  /**
   * Example of the media type. The example object SHOULD be in the correct format as specified by
   * the media type. The example field is mutually exclusive of the examples field. Furthermore, if
   * referencing a schema which contains an example, the example value SHALL override the example
   * provided by the schema.
   */
  public val example: ExampleValue? = null,
  /**
   * Examples of the media type. Each example object SHOULD match the media type and specified
   * schema if present. The examples field is mutually exclusive of the example field. Furthermore,
   * if referencing a schema which contains an example, the examples value SHALL override the
   * example provided by the schema.
   */
  public val examples: Map<String, ReferenceOr<Example>> = emptyMap(),
  /**
   * A map between a property name and its encoding information. The key, being the property name,
   * MUST exist in the schema as a property. The encoding object SHALL only apply to requestBody
   * objects when the media type is multipart or application/x-www-form-urlencoded.
   */
  public val encoding: Map<String, Encoding> = emptyMap(),
  /**
   * OpenAPI 3.2: the schema describing each item within a sequential media type (for example
   * `application/jsonl`, `application/x-ndjson`, `application/json-seq`, `text/event-stream` or
   * `multipart/mixed`). Unlike [schema], which describes the complete content, this applies to
   * every individual item of the sequence.
   */
  public val itemSchema: ReferenceOr<Schema>? = null,
  /**
   * OpenAPI 3.2: a single Encoding Object applied to each item of a sequential multipart media
   * type. Mutually exclusive with [encoding].
   */
  public val itemEncoding: Encoding? = null,
  /**
   * OpenAPI 3.2: positional Encoding Objects applied to the leading parts of a multipart media type,
   * in order. Parts beyond this list are covered by [itemEncoding], if present. Mutually exclusive
   * with [encoding].
   */
  public val prefixEncoding: List<Encoding> = emptyList(),
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
) {
  public companion object {
    internal object Serializer :
      KSerializerWithExtensions<MediaType>(
        generatedSerializer(),
        MediaType::extensions,
        { op, extensions -> op.copy(extensions = extensions) },
      )
  }
}
