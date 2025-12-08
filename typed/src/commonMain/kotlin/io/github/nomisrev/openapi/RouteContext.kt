package io.github.nomisrev.openapi

class RouteContext {
    private val nested: MutableList<Model> = mutableListOf()

    fun add(model: Model) {
        nested.add(model)
    }
}
