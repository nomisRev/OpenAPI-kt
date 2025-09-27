package io.github.nomisrev.openapi

/**
 * The context that is used to resolve references and schemas.
 */
class TypedApiContext(val openAPI: OpenAPI) {
  private val models: Map<String, Model> by lazy {
    openAPI.components.schemas.mapValues { (name, reference) ->
      val schema = when (reference) {
        is ReferenceOr.Reference -> TODO("Support remote references")
        is ReferenceOr.Value<Schema> -> reference.value
      }
      // Use ForComponents strategy during registry initialization to create Model.Reference entries
      schema.toModel(
        ModelResolutionContext(
          NamingContext.Named(name),
          SchemaResolutionStrategy.ForComponents
        )
      )
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
