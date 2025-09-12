package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Named

fun OpenAPI.models(): Set<Model> = TypedApiContext(this).schemas().toSet()

/** Gathers all "top-level", or components schemas. */
private fun TypedApiContext.schemas(): List<Model> =
  openAPI.components.schemas.map { (name, refOrSchema) ->
    when (val resolved = refOrSchema.resolve()) {
      is Resolved.Ref -> throw IllegalStateException("Remote schemas not supported yet.")
      is Resolved.Value -> {
        if (resolved.value.recursiveAnchor == true) {
          with(TypedApiContext(openAPI, name to resolved.value, expanding)) {
            resolved.toModel(Named(name)).value
          }
        } else {
          resolved.toModel(Named(name)).value
        }
      }
    }
  }
