package io.github.nomisrev.openapi

private val IdentifierRegex = Regex("^[A-Za-z_][A-Za-z0-9_]*$")
private val InvalidIdentifierCharsRegex = Regex("[^A-Za-z0-9_]+")

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
    val normalized = when (rawValue) {
        "*" -> "Star"
        "/" -> "Slash"
        else -> rawValue
            .replace("+", " plus ")
            .replace("-", " minus ")
            .replace("*", " star ")
            .replace("/", " slash ")
    }
    val baseName = normalized
        .replace(InvalidIdentifierCharsRegex, " ")
        .toPascalCase()
        .ifBlank { "Value" }
    val candidate = if (baseName.first().isDigit()) "_$baseName" else baseName
    return if (candidate.isValidClassname() && candidate.lowercase() !in KOTLIN_KEYWORDS) candidate else "`$candidate`"
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
