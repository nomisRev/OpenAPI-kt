package io.github.nomisrev.openapi

/**
 * The context that is used to resolve references and schemas.
 *
 * @param currentAnchor when a schema with `$recursiveAnchor: true` was encountered this value is populated,
 * to allow resolving to this schema when encountering referenced `$recursiveRef: "#"`.
 * @param expanding a list of schemas that are currently being expanded. This is used to avoid recursive references,
 * but still completely expanding the model into the typed value instead of returning a `Model.Reference` early.
 */
internal class TypedApiContext(
  val openAPI: OpenAPI,
  val currentAnchor: Pair<String, Schema>? = null,
  val expanding: List<String> = emptyList()
)

context(ctx: TypedApiContext)
internal fun ReferenceOr<Schema>.resolve(): Resolved<Schema> =
  when (this) {
    is ReferenceOr.Value -> Resolved.Value(value)
    is ReferenceOr.Reference -> {
      // Handle JSON Schema $recursiveRef: "#" by resolving to the current anchor
      if (ref == "#") {
        val (anchorName, anchorSchema) =
          requireNotNull(ctx.currentAnchor) {
            "Recursive reference '#' encountered but no active \$recursiveAnchor was found."
          }
        Resolved.Ref(anchorName, anchorSchema)
      } else {
        val name = ref.drop("#/components/schemas/".length)
        val schema =
          requireNotNull(ctx.openAPI.components.schemas[name]) {
            "Schema $this could not be found in. Is it missing?"
          }.valueOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")
        Resolved.Ref(name, schema)
      }
    }
  }

context(ctx: TypedApiContext)
internal tailrec fun ReferenceOr<Response>.get(): Response =
  when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
      val typeName = ref.drop("#/components/responses/".length)
      requireNotNull(ctx.openAPI.components.responses[typeName]) {
        "Response $typeName could not be found in ${ctx.openAPI.components.responses}. Is it missing?"
      }
        .get()
    }
  }

context(ctx: TypedApiContext)
internal tailrec fun ReferenceOr<Parameter>.get(): Parameter =
  when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
      val typeName = ref.drop("#/components/parameters/".length)
      requireNotNull(ctx.openAPI.components.parameters[typeName]) {
        "Parameter $typeName could not be found in ${ctx.openAPI.components.parameters}. Is it missing?"
      }
        .get()
    }
  }

context(ctx: TypedApiContext)
internal tailrec fun ReferenceOr<RequestBody>.get(): RequestBody =
  when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
      val typeName = ref.drop("#/components/requestBodies/".length)
      requireNotNull(ctx.openAPI.components.requestBodies[typeName]) {
        "RequestBody $typeName could not be found in ${ctx.openAPI.components.requestBodies}. Is it missing?"
      }
        .get()
    }
  }

context(ctx: TypedApiContext)
internal tailrec fun ReferenceOr<PathItem>.get(): PathItem =
  when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
      val typeName = ref.drop("#/components/pathItems/".length)
      requireNotNull(ctx.openAPI.components.pathItems[typeName]) {
        "PathItem $typeName could not be found in ${ctx.openAPI.components.pathItems}. Is it missing?"
      }
        .get()
    }
  }

context(ctx: TypedApiContext)
internal tailrec fun ReferenceOr<String>?.get(): String? =
  when (this) {
    is ReferenceOr.Value -> value
    null -> null
    is ReferenceOr.Reference -> {
      val name = ref.drop("#/components/schemas/".length).dropLast("/description".length)
      val schema =
        requireNotNull(ctx.openAPI.components.schemas[name]) {
          "Schema $name could not be found in ${ctx.openAPI.components.schemas}. Is it missing?"
        }
          .valueOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")
      schema.description.get()
    }
  }
