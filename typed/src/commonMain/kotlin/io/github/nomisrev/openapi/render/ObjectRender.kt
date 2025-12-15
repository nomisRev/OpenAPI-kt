package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import kotlin.math.max

context(ctx: Renderer)
fun Model.Object.render(): String = buildString {
    +"@Serializable"
    when (properties.size) {
        0 -> append("data object ${name().simpleName}")
        1 -> valueClass()
        else -> dataClass()
    }
    body()
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.valueClass() {
    if (ctx.jvm) +"@JvmInline"
    append("value class ${name().simpleName}(${properties.single().render()})")
}

context(ctx: Renderer, builder: StringBuilder)
fun Model.Object.dataClass() {
    val simpleName = name().simpleName
    val line = "data class $simpleName(${properties.joinToString { it.render() }})"
    if (line.length <= ctx.maxLineLength) append(line)
    else {
        +"data class $simpleName("
        properties.joinTo(separator = ",\n", postfix = "\n") { "${ctx.indent}${it.render()}" }
        append(")")
    }
}

context(ctx: Renderer)
private fun Model.Object.Property.render(): String = buildString {
    val paramName = baseName.sanitize().dropArraySyntax().toCamelCase()
    if (paramName != baseName) append("@SerialName(\"$baseName\") ")
    if (isRequired) append("@Required ")
    append("val $paramName: ${model.toTypeName().type()}")
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

// TODO: Wip
context(ctx: Renderer)
suspend fun Constraints.Number.requirements(
    name: String
) {
    val min = if (exclusiveMinimum == true) "<" else "<="
    val max = if (exclusiveMaximum == true) "<" else "<="
    val rangeCheck = "$minimum $min $name $maximum $max"
    val rangeMessage = "$name must be $minimum $min $name $maximum $max"
    val multipleOf = "$name % $multipleOf == 0"
    val multipleOfMessage = "$name must be a multiple of $multipleOf"
}
