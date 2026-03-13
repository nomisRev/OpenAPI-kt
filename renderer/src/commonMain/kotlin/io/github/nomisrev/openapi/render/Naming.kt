package io.github.nomisrev.openapi.render

private val arrayPattern = """\[\d*\]""".toRegex()
fun String.dropArraySyntax(): String = replace(arrayPattern, "")

private val negativeNumberRegex = "^-\\d+(\\.\\d+)?\$".toRegex()
fun String.invalidJsName(): Boolean = Regex("`[0-9]").matchesAt(this, 0)

fun toEnumValueName(rawToName: String): String {
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

// sanitize +1 and -1 to `+1` and `-1`
fun String.toParamName(): String = when (this) {
    $$"$type" -> "type"
    else -> {
        val sanitised = replace("$", "")
        val camelCase = if (negativeNumberRegex.matches(sanitised)) sanitised else sanitised.toCamelCase()
        if (camelCase.isValidParamName()) camelCase else "`$camelCase`"
    }
}