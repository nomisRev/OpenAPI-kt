package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import kotlinx.serialization.Serializable

context(ctx: Renderer)
fun Model.Object.render(parentClass: TypeName.Class? = null): String = buildString {
    import(properties.map { (_, prop) -> prop.model })

    +"@Serializable"
    when (properties.size) {
        0 -> append("data object ${name().simpleName}${parentClass.renderAsSuperclass()}")
        1 -> valueClass(parentClass)
        else -> dataClass(parentClass)
    }
    body()
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.valueClass(parentClass: TypeName.Class?) {
    if (ctx.jvm) +"@JvmInline"
    append("value class ${name().simpleName}(${properties.entries.single().render()})${parentClass.renderAsSuperclass()}")
}


context(ctx: Renderer, builder: StringBuilder)
fun Model.Object.dataClass(parentClass: TypeName.Class?) {
    val simpleName = name().simpleName
    val line = "data class $simpleName(${properties.joinToString { it.render() }})${parentClass.renderAsSuperclass()}"
    if (line.length <= ctx.maxLineLength) append(line)
    else {
        +"data class $simpleName("
        properties.joinTo(separator = ",\n", postfix = "\n") { "${ctx.indent}${it.render()}" }
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

context(ctx: Renderer)
fun Map.Entry<String, Model.Object.Property>.render(): String = buildString {
    val paramName = key.sanitize().dropArraySyntax().toCamelCase()
    if (paramName != key) append("@SerialName(\"$key\") ")
    if (value.isRequired && value.model.hasDefault()) append("@Required ")
    append("val $paramName: ${value.model.toTypeName().type()}")
    if (value.model.isNullable) append("?")
    if (value.model.isNullable && !value.isRequired) append(" = null") // TODO default values
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
