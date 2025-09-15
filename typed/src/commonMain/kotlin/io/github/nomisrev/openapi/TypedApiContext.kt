package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.registry.ComponentRegistry

/**
 * The context that is used to resolve references and schemas.
 *
 * @param currentAnchor when a schema with `$recursiveAnchor: true` was encountered this value is populated,
 * to allow resolving to this schema when encountering referenced `$recursiveRef: "#"`.
 * @param expanding a list of schemas that are currently being expanded. This is used to avoid recursive references,
 * but still completely expanding the model into the typed value instead of returning a `Model.Reference` early.
 */
class TypedApiContext(
  val openAPI: OpenAPI,
  val currentAnchor: Pair<String, Schema>? = null,
  val expanding: List<String> = emptyList()
) {
  val registry: ComponentRegistry

  constructor(openAPI: OpenAPI) : this(openAPI, null, emptyList())

  init {
    val cache = mutableMapOf<String, Model>()
    openAPI.components.schemas.forEach { (name, reference) ->
      val schema = when (reference) {
        is ReferenceOr.Reference -> TODO("Support remote references")
        is ReferenceOr.Value<Schema> -> reference.value
      }
      cache[name] = schema.toModel(NamingContext.Named(name))
    }
    registry = ComponentRegistry(cache)
  }
}

