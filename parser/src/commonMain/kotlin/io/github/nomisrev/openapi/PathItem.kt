package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class PathItem(
  /**
   * Allows for an external definition of this path item. The referenced structure MUST be in the
   * format of a [PathItem]. In case a [PathItem] field appears both in the defined object and the
   * referenced object, the behavior is undefined.
   */
  public val ref: String? = null,
  /** An optional, string summary, intended to apply to all operations in this path. */
  public val summary: String? = null,
  /**
   * An optional, string description, intended to apply to all operations in this path. CommonMark
   * syntax MAY be used for rich text representation.
   */
  public val description: String? = null,
  /** A definition of a GET operation on this path. */
  public val get: Operation? = null,
  /** A definition of a PUT operation on this path. */
  public val put: Operation? = null,
  /** A definition of a POST operation on this path. */
  public val post: Operation? = null,
  /** A definition of a DELETE operation on this path. */
  public val delete: Operation? = null,
  /** A definition of a OPTIONS operation on this path. */
  public val options: Operation? = null,
  /** A definition of a HEAD operation on this path. */
  public val head: Operation? = null,
  /** A definition of a PATCH operation on this path. */
  public val patch: Operation? = null,
  /** A definition of a TRACE operation on this path. */
  public val trace: Operation? = null,
  /** An alternative server array to service all operations in this path. */
  public val servers: List<Server>? = null,
  /**
   * A list of parameters that are applicable for all the operations described under this path.
   * These parameters can be overridden at the operation level, but cannot be removed there. The
   * list MUST NOT include duplicated parameters. A unique parameter is defined by a combination of
   * a name and location. The list can use the Reference Object to link to parameters that are
   * defined at the OpenAPI Object's components/parameters.
   */
  public val parameters: List<ReferenceOr<Parameter>> = emptyList(),
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
) {

  public operator fun plus(other: PathItem): PathItem =
    PathItem(
      null,
      null,
      get = get ?: other.get,
      put = put ?: other.put,
      post = post ?: other.post,
      delete = delete ?: other.delete,
      options = options ?: other.options,
      head = head ?: other.head,
      patch = patch ?: other.patch,
      trace = trace ?: other.trace,
      servers = emptyList(),
      parameters = emptyList(),
    )
}
