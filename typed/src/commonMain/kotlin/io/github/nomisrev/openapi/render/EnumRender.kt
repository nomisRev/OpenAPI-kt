package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

context(ctx: Renderer)
fun Model.Enum.render(parentClass: TypeName.Class? = null): String =
    buildString {
        serializable()
        +"enum class ${name().simpleName}${parentClass.renderAsSuperclass()} {"
        +values()
        append("}")
    }

context(ctx: Renderer)
fun Model.Enum.values(): String {
    val rawToName = values.associate { rawName -> Pair(rawName ?: "null", toEnumValueName(rawName ?: "null")) }
    val line = rawToName.entries.joinToString(", ", postfix = ";") { it.value(" ") }.prependIndent(ctx.indent)
    return if (line.length <= ctx.maxLineLength) line
    else rawToName.entries.joinToString(",\n", postfix = ";") { it.value("\n").prependIndent(ctx.indent) }
}
context(ctx: Renderer)
private fun Map.Entry<String, String>.value(separator: String): String {
    val (rawName, valueName) = this
    val isSimple = rawName == valueName
    return if (isSimple) valueName else {
        val jsName =
            if (ctx.js && valueName.invalidJsName()) {
                ctx.import(TypeName.JsName)
                """@JsName("_${valueName.removeSurrounding("`")}")"""
            } else ""
        ctx.import(TypeName.SerialName)
        val annotations = "$jsName@SerialName(\"$rawName\")$separator"
        "$annotations$valueName"
    }
}
