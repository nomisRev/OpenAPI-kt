package io.github.nomisrev.openapi

private val IdentifierRegex = Regex("^[A-Za-z_][A-Za-z0-9_]*$")
private val InvalidIdentifierCharsRegex = Regex("[^A-Za-z0-9_]+")
private val NegativeNumberRegex = Regex("^-\\d+(?:\\.\\d+)?$")
internal val JsIdentifierRegex = Regex("^[A-Za-z_$][A-Za-z0-9_$]*$")
private val InvalidJsIdentifierCharsRegex = Regex("[^A-Za-z0-9_$]+")

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
    // Sign-prefixed names (+1, -1, +count, -offset) must be backtick-escaped to preserve the
    // sign distinction. The generic sanitization pipeline would collapse both "+1" and "-1" to
    // the same identifier ("_1"), causing duplicate property names.
    if (startsWith("+") || startsWith("-")) return "`$this`"
    val sanitized = toCamelCase()
        .replace(InvalidIdentifierCharsRegex, "_")
        .trim('_')
        .ifEmpty { "_" }
        .let { if (it.first().isDigit()) "_$it" else it }
    return if (sanitized.isValidParamName()) sanitized else "`$sanitized`"
}

fun String.unescapeBackticks(): String =
    if (startsWith("`") && endsWith("`") && length >= 2) substring(1, length - 1) else this

fun String.needsJsName(): Boolean {
    val candidate = unescapeBackticks()
    return candidate.firstOrNull()?.isDigit() == true || !JsIdentifierRegex.matches(candidate)
}

fun String.toJsNameValue(): String {
    val candidate = unescapeBackticks()
    val symbolic = candidate
        .replace("*", "star")
        .replace("/", "slash")
        .replace("+", "plus")
        .replace("-", "minus")
    val sanitized = symbolic.replace(InvalidJsIdentifierCharsRegex, "").ifEmpty { "unnamed" }
    return if (sanitized.firstOrNull()?.isDigit() == true) "_$sanitized" else sanitized
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

fun toEnumValueName(rawValue: String): String =
    when (rawValue) {
        "*" -> "Star"
        "/" -> "Slash"
        else -> {
            val pascalCase = if (NegativeNumberRegex.matches(rawValue)) rawValue else rawValue.toPascalCase()
            val sanitized = pascalCase
                .let { if (it.startsWith("[")) it.drop(1) else it }
                .let { if (it.endsWith("]")) it.dropLast(1) else it }
            when {
                pascalCase.isValidClassname() -> pascalCase
                sanitized.isValidClassname() -> sanitized
                else -> "`$sanitized`"
            }
        }
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
