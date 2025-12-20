package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.transformers.isTopLevel

context(ctx: Renderer)
fun Model.Union.render(): String = buildString {
    if (discriminator != null) {
        ctx.import(TypeName.JsonClassDiscriminator)
        +"@JsonClassDiscriminator(${discriminator.stringValue()})"
    }
    serializable()
    +"sealed interface ${name().simpleName} {"
    +body().prependIndent(ctx.indent)
    append("}")
}

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.valueClass(): String {
    val typeName = model.toTypeName()
    val className = discriminator?.toPascalCase() ?: "Case${typeName.name()}"
    import(model)

    return buildString {
        serializable()
        if (ctx.jvm) jvmInline()
        append("value class $className(val value: ${typeName.type()}) : ${union.name().simpleName}")
    }
}

context(ctx: Renderer)
private fun Model.Union.body(): String =
    cases.joinToString("\n\n") { case ->
        val serialName = if (case.discriminator != null) {
            ctx.import(TypeName.SerialName)
            "@SerialName(\"${case.discriminator}\")\n"
        } else ""
        val renderedCase: String = case.render()
        "$serialName$renderedCase"
    }

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.render(): String {
    return when (model) {
        is Model.ContextHolder if model.context.isTopLevel() -> valueClass()
        is Model.Primitive.Unit -> """
                   |@Serializable
                   |data object Empty : ${union.name().simpleName}
                """.trimMargin()

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
    }
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