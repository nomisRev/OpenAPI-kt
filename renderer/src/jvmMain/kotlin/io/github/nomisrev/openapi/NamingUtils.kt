package io.github.nomisrev.openapi

private val IdentifierRegex = Regex("^[A-Za-z_][A-Za-z0-9_]*$")
private val InvalidIdentifierCharsRegex = Regex("[^A-Za-z0-9_]+")
private val NegativeNumberRegex = Regex("^-\\d+(?:\\.\\d+)?$")

val KOTLIN_KEYWORDS: Set<String> = setOf(
    "as",
    "break",
    "class",
    "continue",
    "do",
    "else",
    "false",
    "for",
    "fun",
    "if",
    "in",
    "interface",
    "is",
    "null",
    "object",
    "package",
    "return",
    "super",
    "this",
    "throw",
    "true",
    "try",
    "typealias",
    "val",
    "var",
    "when",
    "while",
)

fun String.toParamName(): String {
    val sanitized = toCamelCase()
        .replace(InvalidIdentifierCharsRegex, "_")
        .trim('_')
        .ifEmpty { "_" }
        .let { if (it.first().isDigit()) "_$it" else it }
    return if (sanitized.isValidParamName()) sanitized else "`$sanitized`"
}

fun String.toCamelCase(): String {
    val words = splitToWords()
        .map(String::trim)
        .filter(String::isNotEmpty)
    if (words.isEmpty()) return this
    val head = words.first().lowercase()
    val tail = words.drop(1).joinToString("") { word ->
        word.lowercase().replaceFirstChar { char -> char.titlecase() }
    }
    return head + tail
}

fun toEnumValueName(rawValue: String): String {
    if (rawValue == "*") return "Star"
    if (rawValue == "/") return "Slash"

    val pascalCase = if (NegativeNumberRegex.matches(rawValue)) rawValue else rawValue.toPascalCase()
    if (pascalCase.isValidClassname()) return pascalCase

    val sanitized = pascalCase
        .let { if (it.startsWith("[")) it.drop(1) else it }
        .let { if (it.endsWith("]")) it.dropLast(1) else it }
    return if (sanitized.isValidClassname()) sanitized else "`$sanitized`"
}

fun String.stringValue(): String = escapeKotlinString()

fun String.escapeKotlinString(): String = buildString {
    for (char in this@escapeKotlinString) {
        append(
            when (char) {
                '\\' -> "\\\\"
                '"' -> "\\\""
                '\n' -> "\\n"
                '\r' -> "\\r"
                '\t' -> "\\t"
                '\b' -> "\\b"
                '\u000C' -> "\\f"
                '$' -> "\\$"
                else -> char.toString()
            }
        )
    }
}

fun String.isValidClassname(): Boolean = matches(IdentifierRegex)

fun String.isValidParamName(): Boolean = matches(IdentifierRegex) && lowercase() !in KOTLIN_KEYWORDS

fun String.sanitize(): String {
    val sanitized = lowercase()
        .replace(InvalidIdentifierCharsRegex, "_")
        .trim('_')
        .ifEmpty { "_" }
        .let { if (it.first().isDigit()) "_$it" else it }
    return if (sanitized in KOTLIN_KEYWORDS) "${sanitized}_" else sanitized
}
