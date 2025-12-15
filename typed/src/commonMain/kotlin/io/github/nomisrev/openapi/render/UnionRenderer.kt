package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

context(builder: StringBuilder)
operator fun String?.unaryPlus() {
    if (this != null) builder.appendLine(this)
}

context(builder: StringBuilder)
fun append(line: String?) {
    if (line != null) builder.append(line)
}

context(builder: StringBuilder)
fun <A> Iterable<A>.joinTo(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((A) -> CharSequence)? = null
) {
    joinTo(builder, separator, prefix, postfix, limit, truncated, transform)
}

fun String.stringValue(): String {
    var max = 0
    var count = 0
    for (c in this)
        if (c != '$' || count++ <= max) Unit
        else {
            max = count
        }
    val dollars = "$".repeat(count + 1)
    return "$dollars\"$this\""
}

context(ctx: Renderer)
fun Model.Union.render(): String = buildString {
    if (discriminator != null) +"@JsonClassDiscriminator(${discriminator.stringValue()})"
    +"@Serializable"
    +"sealed interface ${name().simpleName} {"
    +body().prependIndent(ctx.indent)
    append("}")
}

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.valueClass(): String {
    val className = discriminator?.toPascalCase() ?: "Case${model.toTypeName().name()}"

    return """
    |@Serializable
    |@JvmInline
    |value class $className(val value: ${model.toTypeName().type()}) : ${union.name().simpleName}
    """.trimMargin()
}

private class Index {
    var index = 1
}

context(ctx: Renderer)
private fun Model.Union.body(): String {
    return cases.joinToString("\n\n") { case ->
        val serialName = if (case.discriminator != null) "@SerialName(\"${case.discriminator}\")\n" else ""
        val renderedCase: String = case.render()
        "$serialName$renderedCase"
    }
}

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.render(): String {
    return when (model) {
        is Model.ContextHolder if model.context.head is NamingContext.Reference && model.context.nested.isEmpty() -> valueClass()
        is Model.Reference -> valueClass()
        is Model.Primitive,
        is Model.DateTime,
        is Model.ByteArray,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Date -> valueClass()

        is Model.DiscriminatedObject -> TODO("Nested DiscriminatedObject not supported in Union")
        is Model.Union -> TODO("Inline defined nested Union not yet supported in Union")
        is Model.Collection -> valueClass()

        is Model.Object -> model.render(union.name())
        is Model.Enum -> model.render(union.name())
        is Model.Primitive.Unit -> """
                   |@Serializable
                   |data object Empty : ${union.name().simpleName}
                """.trimMargin()
    }
}

fun Model.contextOrNull(): NamingContext? = when (this) {
    is Model.DiscriminatedObject -> context
    is Model.Enum -> context
    is Model.Object -> context
    is Model.Union -> context
    is Model.Reference -> context
    is Model.Collection -> inner.contextOrNull()
    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Uuid,
    is Model.Primitive -> null
}


private fun TypeName.name(depth: Int = 0): String = when (this) {
    is TypeName.Collection -> type.name(depth + 1)
    is TypeName.Class -> {
        val postfix =
            if (depth == 0) ""
            else if (simpleName.endsWith("s")) (0..depth).joinToString(separator = "") { "List" }
            else {
                val postfix = (0..depth - 2).joinToString(separator = "") { "List" }
                "s$postfix"
            }
        simpleName + postfix
    }
}