package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

fun TypeName.Class?.renderSuperclass(): String =
    this?.simpleName?.let { " : $it" } ?: ""

context(ctx: Renderer)
fun Model.Enum.render(parentClass: TypeName.Class? = null): String = """
    |@Serializable
    |enum class ${name().simpleName}${parentClass.renderSuperclass()} {
    |${values()}
    |}
    """.trimMargin()

context(ctx: Renderer)
fun Model.Enum.values(): String {
    val rawToName = values.associate { rawName -> Pair(rawName ?: "null", toEnumValueName(rawName ?: "null")) }
    val line = rawToName.entries.joinToString(", ", postfix = ";") { it.value(" ") }.prependIndent(ctx.indent)
    return if (line.length <= ctx.maxLineLength) line
    else rawToName.entries.joinToString(",\n", postfix = ";") { it.value("\n").prependIndent(ctx.indent) }
}

private fun String.invalidJsName(): Boolean =
    Regex("`[0-9]").matchesAt(this, 0)

context(ctx: Renderer)
private fun Map.Entry<String, String>.value(separator: String): String {
    val (rawName, valueName) = this
    val isSimple = rawName == valueName
    return if (isSimple) valueName else {
        val jsName =
            if (ctx.js && valueName.invalidJsName()) "@JsName(\"_\${valueName.removeSurrounding(\"`\")}\")$separator" else ""
        val annotations = "$jsName@SerialName(\"$rawName\")$separator"
        "$annotations$valueName"
    }
}

private val negativeNumberRegex = "^-\\d+(\\.\\d+)?\$".toRegex()

private fun toEnumValueName(rawToName: String): String {
    if (rawToName == "*") return "Star"
    if (rawToName == "/") return "Slash"
    val pascalCase = if (negativeNumberRegex.matches(rawToName)) rawToName else rawToName.toPascalCase()
    return if (pascalCase.isValidClassname()) pascalCase
    else {
        val sanitise =
            pascalCase
                .run { if (startsWith("[")) drop(1) else this }
                .run { if (endsWith("]")) dropLast(1) else this }

        if (sanitise.isValidClassname()) sanitise else "`$sanitise`"
    }
}