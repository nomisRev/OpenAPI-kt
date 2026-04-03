package io.github.nomisrev.openapi.pipeline

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext

interface ModelInterceptor {
    context(ctx: Registry)
    suspend fun intercept(model: Model, context: SchemaContext): Model
}
