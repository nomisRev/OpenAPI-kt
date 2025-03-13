package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * The Link object represents a possible design-time link for a response. The presence of a link
 * does not guarantee the caller's ability to successfully invoke it, rather it provides a known
 * relationship and traversal mechanism between responses and other operations.
 */
@Serializable
public data class Link(
  /**
   * A relative or absolute URI reference to an OAS operation. This field is mutually exclusive of
   * the '_linkOperationId' field, and MUST point to an 'Operation' Object. Relative
   * '_linkOperationRef' values MAY be used to locate an existing 'Operation' Object in the OpenAPI
   * definition.
   */
  public val operationRef: String? = null,
  /**
   * The name of an /existing/, resolvable OAS operation, as defined with a unique
   * '_operationOperationId'. This field is mutually exclusive of the '_linkOperationRef' field.
   */
  public val operationId: String? = null,
  /**
   * A map representing parameters to pass to an operation as specified with '_linkOperationId' or
   * identified via '_linkOperationRef'. The key is the parameter name to be used, whereas the value
   * can be a constant or an expression to be evaluated and passed to the linked operation. The
   * parameter name can be qualified using the parameter location @[{in}.]{name}@ for operations
   * that use the same parameter name in different locations (e.g. path.id).
   */
  public val parameters: Map<String, ExpressionOrValue>,
  /**
   * A literal value or @{expression}@ to use as a request body when calling the target operation.
   */
  public val requestBody: ExpressionOrValue,
  /** A description of the link. */
  public val description: String? = null,
  /** A server object to be used by the target operation. */
  public val server: Server?,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
)
