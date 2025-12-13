package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

context(ctx: Renderer)
fun Model.Object.render(className: ClassName = className()): String = buildString {
    +"@Serializable"
    when (properties.size) {
        0 -> append("data object ${className.render()}")
        1 -> valueClass(className)
        else -> dataClass(className)
    }
    body()
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.valueClass(className: ClassName) {
    if (ctx.jvm) +"@JvmInline"
    append("value class ${className.render()}(${properties.single().render()})")
}

context(ctx: Renderer, builder: StringBuilder)
fun Model.Object.dataClass(className: ClassName) {
    val line = "data class ${!className}(${properties.joinToString { it.render() }})"
    if (line.length <= ctx.maxLineLength) append(line)
    else {
        +"data class ${className().render()}("
        properties.joinTo(separator = ",\n", postfix = "\n") { "${ctx.indent}${it.render()}" }
        append(")")
    }
}

context(ctx: Renderer)
private fun Model.Object.Property.render(): String = buildString {
    val paramName = baseName.sanitize().dropArraySyntax().toCamelCase()
    if (paramName != baseName) append("@SerialName(\"$baseName\") ")
    if (isRequired) append("@Required ")
    append("val $paramName: ${!model.className()}")
    if (model.isNullable) append("?")
    if (model.isNullable && !isRequired) append(" = null")
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.body() {
    if (inline.isNotEmpty()) {
        +" {"
        inline.joinTo(separator = "\n\n", postfix = "\n") { it.render().prependIndent(ctx.indent) }
        append("}")
    }
}
