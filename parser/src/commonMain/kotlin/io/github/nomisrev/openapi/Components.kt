@file:Suppress("OPT_IN_USAGE")

package io.github.nomisrev.openapi

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Holds a set of reusable objects for different aspects of the OAS. All objects defined within the
 * components object will have no effect on the API unless they are explicitly referenced from
 * properties outside the components object.
 */
// TODO, need `KeepGeneratedSerializer` or manually define instance...
@Serializable(Components.Companion.Serializer::class)
@OptIn(InternalSerializationApi::class)
@KeepGeneratedSerializer
public data class Components(
  public val schemas: Map<String, ReferenceOr<Schema>> = emptyMap(),
  public val responses: Map<String, ReferenceOr<Response>> = emptyMap(),
  public val parameters: Map<String, ReferenceOr<Parameter>> = emptyMap(),
  public val examples: Map<String, ReferenceOr<Example>> = emptyMap(),
  public val requestBodies: Map<String, ReferenceOr<RequestBody>> = emptyMap(),
  public val headers: Map<String, ReferenceOr<Header>> = emptyMap(),
  //    val securitySchemes: Definitions<SecurityScheme>,
  public val links: Map<String, Link> = emptyMap(),
  public val callbacks: Map<String, Callback> = emptyMap(),
  public val pathItems: Map<String, ReferenceOr<PathItem>> = emptyMap(),
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
) {
  public companion object {
    internal object Serializer :
      KSerializerWithExtensions<Components>(
        generatedSerializer(),
        Components::extensions,
        { op, extensions -> op.copy(extensions = extensions) },
      )
  }
}
