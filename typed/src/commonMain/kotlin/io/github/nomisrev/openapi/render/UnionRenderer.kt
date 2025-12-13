package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.body

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
    +"sealed interface ${className().render()} {"
    +body().prependIndent(ctx.indent)
    +inlineOrNull()?.prependIndent(ctx.indent)
    append("}")
}

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.valueClass(): String {
    val className = discriminator?.toPascalCase() ?: "Case${model.className().render()}"
    return """
    |@Serializable
    |@JvmInline
    |value class $className(val value: ${model.className().render()}) : ${union.className().render()}
    """.trimMargin()
}

context(ctx: Renderer)
private fun Model.Union.body(): String {
    var index = 1
    return cases.joinToString("\n\n") { case ->
        val serialName = if (case.discriminator != null) "@SerialName(\"${case.discriminator}\")\n" else ""
        val ctxOrNull = case.model.contextOrNull()
        val renderedCase: String = when (case.model) {
            is Model.Reference -> case.valueClass()
            else if (ctxOrNull != null && ctxOrNull is NamingContext.Reference) -> case.valueClass()
            is Model.Primitive,
            is Model.DateTime,
            is Model.ByteArray,
            is Model.Union,
            is Model.FreeFormJson,
            is Model.Uuid,
            is Model.Date -> case.valueClass()

            is Model.Enum -> {
                val name = case.model.values.joinToString(
                    prefix = "",
                    separator = "Or"
                ) { it?.replaceFirstChar(Char::uppercaseChar) ?: "" }

                """
                |@Serializable
                |enum class $name : ${className().render()} {
                |${case.model.values()}
                |}
                """.trimMargin()
            }

            is Model.Primitive.Unit -> """
                   |@Serializable
                   |data object Empty : ${className().render()}
                """.trimMargin()

            is Model.Collection -> TODO()
            is Model.DiscriminatedObject -> TODO("Currently discriminated objects are not supported in unions")
            is Model.Object -> {
                val discriminatorName = case.discriminator?.toPascalCase()
                val enumSpecialName = case.model.properties
                    .firstOrNull { it.baseName in setOf("type", "event", $$"$type") }
                    ?.let { (it.model as? Model.Enum)?.values?.singleOrNull() }

                val name = discriminatorName ?: enumSpecialName ?: "Case${index++}"

                case.model.render(ClassName("ignore-always-nested", name)) + " : ${className().render()}"
            }
        }

        "$serialName$renderedCase"
    }
}

context(ctx: Renderer)
private fun Model.Union.inlineOrNull(): String? {
    val inline = inline.filter { it !is Model.Object }
    if (inline.isEmpty()) return null
    return null
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

