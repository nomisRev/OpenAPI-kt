package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import kotlinx.serialization.SerialName

context(ctx: Renderer)
fun Model.Enum.render(): String =
    """|@Serializable
       |enum class ${className().render()} {
       |${values()}
       |}""".trimMargin()

context(ctx: Renderer)
private fun Model.Enum.values(): String {
    val rawToName = values.associate { rawName -> Pair(rawName ?: "null", toEnumValueName(rawName ?: "null")) }
    val line = rawToName.entries.joinToString(", ", prefix = ctx.indent, postfix = ";") { it.value() }
    return if (line.length <= ctx.maxLineLength) line else rawToName.entries.joinToString(
        ",\n",
        postfix = ";"
    ) { it.value() }
        .prependIndent(ctx.indent)
}

context(ctx: Renderer)
private fun Map.Entry<String, String>.value(): String {
    val (rawName, valueName) = this
    val isSimple = rawName == valueName
    return if (isSimple) valueName else {
        val annotations = if (ctx.js && Regex("`[0-9]").matchesAt(valueName, 0)) {
            """|@JsName("_${valueName.removeSurrounding("`")}")
               |@SerialName("$rawName")
            """.trimIndent()
        } else {
            """@SerialName("$rawName")"""
        }

        """|$annotations
           |$valueName""".trimMargin()
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