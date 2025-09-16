package io.github.nomisrev.openapi

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
  val currentAnchor: Pair<String, Schema>? = null
) {
  private val models: Map<String, Model> by lazy {
    openAPI.components.schemas.mapValues { (name, reference) ->
      val schema = when (reference) {
        is ReferenceOr.Reference -> TODO("Support remote references")
        is ReferenceOr.Value<Schema> -> reference.value
      }
      // Use ForComponents strategy during registry initialization to create Model.Reference entries
      schema.toModel(ModelResolutionContext(NamingContext.Named(name), SchemaResolutionStrategy.ForComponents))
    }
  }

  fun get(referenceOr: ReferenceOr.Reference): Model? =
    models[referenceOr.ref.drop("#/components/schemas/".length)]

  fun schema(referenceOr: ReferenceOr.Reference): Schema =
    requireNotNull(openAPI.components.schemas[referenceOr.schemaName]) {
      "Schema $this could not be found in. Is it missing?"
    }.valueOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")

  fun get(name: String): Model? = models[name]
  fun models(): Set<Model> = models.values.toSet()
}

internal val ReferenceOr.Reference.schemaName: String
  get() = ref.drop("#/components/schemas/".length)
