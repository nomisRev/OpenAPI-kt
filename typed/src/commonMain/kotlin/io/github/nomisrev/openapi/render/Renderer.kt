package io.github.nomisrev.openapi.render

class Renderer(
    val maxLineLength: Int,
    val indentSize: Int,
    val jvm: Boolean,
    val js: Boolean,
) {
    val packageName: String = "io.github.nomisrev"
    val indent: String get() = " ".repeat(indentSize)
}

fun <A> renderer(block: context(Renderer) () -> A): Pair<A, Set<TypeName>> {
    val buffer = mutableSetOf<TypeName>()
    val ctx = Renderer(120, 4, jvm = true, js = true)
    return Pair(block(ctx), buffer)
}