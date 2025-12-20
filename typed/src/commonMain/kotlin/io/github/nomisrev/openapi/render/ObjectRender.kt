package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model

context(ctx: Renderer, builder: StringBuilder)
fun serializable() {
    ctx.import(TypeName.Serializable)
    +"@Serializable"
}

context(ctx: Renderer, builder: StringBuilder)
fun jvmInline() {
    ctx.import(TypeName.JvmInline)
    +"@JvmInline"
}

context(ctx: Renderer, builder: StringBuilder)
fun jsName() {
    ctx.import(TypeName.JvmInline)
    +"@JvmInline"
}

context(ctx: Renderer)
fun Model.Object.render(
    parentClass: TypeName.Class? = null,
    baseProperties: Set<String> = emptySet()
): String = buildString {
    import(properties.map { (_, prop) -> prop.model })

    serializable()
    when (properties.size) {
        0 -> append("data object ${name().simpleName}${parentClass.renderAsSuperclass()}")
        1 -> valueClass(parentClass, baseProperties)
        else -> dataClass(parentClass, baseProperties)
    }
    body()
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.valueClass(parentClass: TypeName.Class?, baseProperties: Set<String>) {
    if (ctx.jvm) jvmInline()
    append(
        "value class ${name().simpleName}(${
            properties.entries.single().render(baseProperties)
        })${parentClass.renderAsSuperclass()}"
    )
}


context(ctx: Renderer, builder: StringBuilder)
fun Model.Object.dataClass(parentClass: TypeName.Class?, baseProperties: Set<String>) {
    val simpleName = name().simpleName
    val line =
        "data class $simpleName(${properties.joinToString { it.render(baseProperties) }})${parentClass.renderAsSuperclass()}"
    if (line.length <= ctx.maxLineLength) append(line)
    else {
        +"data class $simpleName("
        properties.joinTo(separator = ",\n", postfix = "\n") { "${ctx.indent}${it.render(baseProperties)}" }
        append(")${parentClass.renderAsSuperclass()}")
    }
}

private fun Model.hasDefault(): Boolean = when (this) {
    is Model.Enum -> default != null
    is Model.Collection -> default != null
    is Model.Primitive.Boolean -> default != null
    is Model.Primitive.Double -> default != null
    is Model.Primitive.Float -> default != null
    is Model.Primitive.Int -> default != null
    is Model.Primitive.Long -> default != null
    is Model.Primitive.String -> default != null
    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Primitive.Unit,
    is Model.Reference,
    is Model.Union,
    is Model.Uuid,
    is Model.DiscriminatedObject -> false
}


fun String.toParamName(): String = when (this) {
    $$"$type" -> "type"
    else -> sanitize().dropArraySyntax().toCamelCase()
}

context(ctx: Renderer)
fun Map.Entry<String, Model.Object.Property>.render(
    baseProperties: Set<String>,
    defaultValue: Boolean = true
): String = render(key, value, baseProperties, defaultValue)

context(ctx: Renderer)
fun render(
    baseName: String,
    prop: Model.Object.Property,
    baseProperties: Set<String>,
    defaultValue: Boolean = true
): String = buildString {
    val paramName = baseName.toParamName()

    if (paramName != baseName) {
        ctx.import(TypeName.SerialName)
        append("@SerialName(${baseName.stringValue()}) ")
    }

    val hasDefault = prop.model.hasDefault()
    if (prop.isRequired && hasDefault) {
        ctx.import(TypeName.Required)
        append("@Required ")
    }

    if (baseName in baseProperties) append("override ")

    append("val $paramName: ${prop.model.toTypeName().type()}")

    if (prop.model.isNullable || !prop.isRequired) append("?")

    when {
        !defaultValue -> {}
        prop.model.isNullable && prop.isRequired && !hasDefault -> {}
        prop.model.isNullable || !prop.isRequired -> append(" = null")
    }
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
