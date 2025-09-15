package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.ReferenceOr

class ComponentRegistry(private val models: Map<String, Model>) {
  fun get(referenceOr: ReferenceOr.Reference): Model =
    models[referenceOr.ref.drop("#/components/schemas/".length)]!!

  fun get(name: String): Model? = models[name]
  fun all(): Set<Model> = models.values.toSet()
}
