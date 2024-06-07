package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** A single encoding definition applied to a single schema property. */
@Serializable
public data class Encoding(
  /**
   * The Content-Type for encoding a specific property. Default value depends on the property type:
   * - for string with format being binary – application/octet-stream;
   * - for other primitive types – text/plain
   * - for object - application/json
   * - for array – the default is defined based on the inner type. The value can be a specific media
   *   type (e.g. application/json), a wildcard media type (e.g. image&#47;&#42;), or a
   *   comma-separated list of the two types.
   */
  public val contentType: String, // Could be arrow.endpoint.model.MediaType
  /**
   * A map allowing additional information to be provided as headers, for example
   * Content-Disposition. Content-Type is described separately and SHALL be ignored in this section.
   * This property SHALL be ignored if the request body media type is not a multipart
   */
  public val headers: Map<String, ReferenceOr<Header>>,
  /**
   * Describes how a specific property value will be serialized depending on its type. See [Style]
   * for details on the style property. The behavior follows the same values as query parameters,
   * including default values. This property SHALL be ignored if the request body media type is not
   * application/x-www-form-urlencoded.
   */
  public val style: String? = null,
  /**
   * When this is true, property values of type array or object generate separate parameters for
   * each value of the array, or key-value-pair of the map. For other types of properties this
   * property has no effect. When style is form, the default value is true. For all other styles,
   * the default value is false. This property SHALL be ignored if the request body media type is
   * not application/x-www-form-urlencoded.
   */
  public val explode: Boolean, // = style?.let { it == Style.form.name } ?: false,
  /**
   * Determines whether the parameter value SHOULD allow reserved characters, as defined by RFC3986
   * :/?#[]@!$&'()*+,;= to be included without percent-encoding. The default value is false. This
   * property SHALL be ignored if the request body media type is not
   * application/x-www-form-urlencoded.
   */
  public val allowReserved: Boolean,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap()
) {
  public companion object {
    internal object Serializer :
      KSerializerWithExtensions<Encoding>(
        OpenAPI.Json,
        serializer(),
        Encoding::extensions,
        { op, extensions -> op.copy(extensions = extensions) }
      )
  }
}
