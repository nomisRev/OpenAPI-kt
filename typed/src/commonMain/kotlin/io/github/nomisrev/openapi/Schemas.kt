package io.github.nomisrev.openapi

fun OpenAPI.models(): Set<Model> =
  TypedApiContext(this).models()
