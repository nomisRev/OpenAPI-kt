package io.github.nomisrev.openapi

internal fun String.toPascalCase(): String {
  val words = split("_", "-")
  return when (words.size) {
    1 -> words[0].capitalize()
    else -> buildString {
      append(words[0].capitalize())
      for (i in 1 until words.size) {
        append(words[i].capitalize())
      }
    }
  }
}

internal fun String.toCamelCase(): String {
  val words = replace("[]", "").split("_")
  return when (words.size) {
    1 -> words[0]
    else -> buildString {
      append(words[0].decapitalize())
      for (i in 1 until words.size) {
        append(words[i].capitalize())
      }
    }
  }
}

internal fun String.decapitalize(): String =
  replaceFirstChar { it.lowercase() }

internal fun String.capitalize(): String =
  replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun String.sanitize(delimiter: String = ".", prefix: String = "") =
  splitToSequence(delimiter).joinToString(delimiter, prefix) { if (it in KOTLIN_KEYWORDS) "`$it`" else it }

private val KOTLIN_KEYWORDS = setOf(
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