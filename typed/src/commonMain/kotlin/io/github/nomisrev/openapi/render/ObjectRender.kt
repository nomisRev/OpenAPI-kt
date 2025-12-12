package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

context(ctx: Renderer)
fun Model.Object.render(): String = when (properties.size) {
    0 -> "data object ${className().render()}"
    1 -> valueClass()
    else -> {
        val line = singleLine()
        if (line.length <= ctx.maxLineLength) line else multiline()
    }
} + body()

context(ctx: Renderer)
private fun Model.Object.valueClass(): String {
    val annotations = if (ctx.jvm) {
        """|@Serializable
           |@JvmInline""".trimMargin()
    } else {
        "@Serializable"
    }
    return """|$annotations
              |value class ${className().render()}(${properties.single().render()})""".trimMargin()
}

context(ctx: Renderer)
private fun Model.Object.singleLine(): String = "data class ${!className()}(${properties.joinToString { it.render() }})"

context(ctx: Renderer)
private fun Model.Object.multiline(): String =
    """|@Serializable
       |data class ${className().render()}(
       |${properties.joinToString(separator = ",\n    ", prefix = ctx.indent) { it.render() }}
       |)""".trimMargin()

context(ctx: Renderer)
private fun Model.Object.Property.render(): String {
    val paramName = baseName.sanitize().dropArraySyntax().toCamelCase()
    val serialName = if (paramName != baseName) "@SerialName(\"$baseName\") " else ""
    val required = if (isRequired) "@Required " else ""
    val default = if (model.isNullable && !isRequired) " = null" else ""
    val nullable = if (model.isNullable) "?" else ""
    return "$serialName${required}val $paramName: ${!model.className()}$nullable$default"
}

context(ctx: Renderer)
private fun Model.Object.body(): String =
    if (inline.isNotEmpty()) {
        """| {
           |
           |${inline.joinToString(separator = "\n\n") { it.render() }.prependIndent(ctx.indent)}
           |}""".trimMargin()
    } else ""
