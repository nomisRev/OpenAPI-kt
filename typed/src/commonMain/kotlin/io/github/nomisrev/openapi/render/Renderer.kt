package io.github.nomisrev.openapi.render

class Renderer(
    val maxLineLength: Int,
    val indentSize: Int,
    val jvm: Boolean,
    val js: Boolean,
    imports: Imports
) : Imports by imports {
    val indent: String get() = " ".repeat(indentSize)
}

fun interface Imports {
    fun import(name: ClassName): String
}

fun <A> renderer(block: context(Renderer) () -> A): Pair<A, Set<ClassName>> {
    val buffer = mutableSetOf<ClassName>()
    val imports = Imports { name ->
        buffer.add(name)
        name.render()
    }
    val ctx = Renderer(120, 4, jvm = true, js = true, imports = imports)
    return Pair(block(ctx), buffer)
}