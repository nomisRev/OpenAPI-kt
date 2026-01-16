package io.github.nomisrev.openapi.render

enum class LeniencyMode {
    FailFast, Ignore;
}

class Config(
    val maxLineLength: Int,
    val indentSize: Int,
    val jvm: Boolean,
    val js: Boolean,
    val packageName: String = "io.github.nomisrev",
    // TODO
    val leniencyMode: LeniencyMode = LeniencyMode.FailFast,
)

// We use a 'mix-in' pattern here for Importer to avoid having to use `context(Renderer, Importer)`
class Renderer(val config: Config, import: Importer) : Importer by import {
    val maxLineLength: Int get() = config.maxLineLength
    val indentSize: Int get() = config.indentSize
    val jvm: Boolean get() = config.jvm
    val js: Boolean get() = config.js
    val packageName: String get() = config.packageName
    val indent: String get() = " ".repeat(indentSize)
}

fun <A> renderer(block: context(Renderer) () -> A): Pair<A, Set<Import>> {
    val buffer = mutableSetOf<Import>()
    val ctx = Renderer(Config(120, 4, jvm = true, js = true, "io.github.nomisrev"), import = importer(buffer))
    return Pair(block(ctx), buffer)
}

private fun importer(buffer: MutableSet<Import>) = object : Importer {
    fun add(typeName: TypeName) {
        when (typeName) {
            TypeName.String,
            TypeName.Int,
            TypeName.Long,
            TypeName.Float,
            TypeName.Double,
            TypeName.ByteArray,
            TypeName.Unit -> Unit

            is TypeName.Collection -> add(typeName.type)
            is TypeName.Class -> buffer.add(typeName)
        }
    }

    override fun import(import: Import) {
        when (import) {
            is TopLevelFunction -> buffer.add(import)
            is TypeName -> add(import)
        }
    }
}
