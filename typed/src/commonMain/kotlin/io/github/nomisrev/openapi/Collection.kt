package io.github.nomisrev.openapi

// TODO: Nested??
context(ctx: ApiCtx)
suspend fun ResolvedSchema.collection(context: SchemaContext): Model {
    val schema = when (this) {
        is ResolvedSchema.Reference -> schema
        is ResolvedSchema.Value -> schema
    }

    return when (val items = schema.items?.resolve(context)) {
        // TODO should list be nullable? Or resulting JsArray? Or default empty value?
        null -> Model.FreeFormJson(description = null, constraint = null, isNullable = false)

        // TODO these should be recursing, and now cannot resolve Schema()
        is ResolvedSchema.Reference -> Model.Collection.List(items.primitive(), emptyList(), null, null, false)
        is ResolvedSchema.Value -> Model.Collection.List(items.primitive(), emptyList(), null, null, false)
    }
}
