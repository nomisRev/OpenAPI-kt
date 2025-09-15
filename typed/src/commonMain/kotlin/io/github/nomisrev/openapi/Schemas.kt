package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.registry.ComponentRegistry

fun OpenAPI.models(): Set<Model> =
  TypedApiContext(this).registry.all()

/** Gathers all "top-level", or components schemas. */
fun TypedApiContext.models(): Set<Model> = registry.all()
