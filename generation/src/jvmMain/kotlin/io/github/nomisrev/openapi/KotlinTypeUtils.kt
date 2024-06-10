package io.github.nomisrev.openapi

private val classNameRegex = Regex("^[a-zA-Z_$][a-zA-Z\\d_$]*$")

internal fun String.isValidClassname(): Boolean = classNameRegex.matches(this)

internal fun String.sanitize(delimiter: String = ".", prefix: String = ""): String =
  splitToSequence(delimiter).joinToString(delimiter, prefix) {
    if (it in KOTLIN_KEYWORDS) "`$it`" else it
  }

// This list only contains words that need to be escaped.
private val KOTLIN_KEYWORDS =
  setOf(
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
    "typeof",
    "val",
    "var",
    "when",
    "while",
  )
