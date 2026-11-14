package io.github.nomisrev.openapi

/**
 * The Kotlin entry names of one enum, chosen for all of its values together ([enumEntryNames]). [nameOf] is the entry
 * of a wire value, and every place that names an entry (the declaration, `@SerialName`, the serializers, a default
 * value) asks this table, never [toEnumValueName] alone: two values of one enum can have one name there.
 */
internal class EnumEntryNames(
    private val byWireValue: Map<String, String>,
    val renames: List<EnumEntryRename>,
) {
    /** The entry of [wireValue]; the name of the value alone for one that is not a value of the enum. */
    fun nameOf(wireValue: String): String = byWireValue[wireValue] ?: toEnumValueName(wireValue)
}

/** A value whose entry is not the name its own text gives it, because another value of the enum has that name. */
internal data class EnumEntryRename(
    val wireValue: String,
    val entryName: String,
    val plainName: String,
    val reason: EnumRenameReason,
) {
    val explanation: String
        get() = when (reason) {
            EnumRenameReason.SpelledSymbols ->
                "another value has the same Kotlin name ('${plainName.unescapeBackticks()}'), " +
                    "so the symbols of this value are spelled out."
            EnumRenameReason.Numbered ->
                "an earlier value has the same Kotlin name, so the first free number from 2 is appended."
        }
}

internal enum class EnumRenameReason { SpelledSymbols, Numbered }

internal fun Model.Enum.entryNames(): EnumEntryNames = enumEntryNames(values.map { it.wireValue() })

/**
 * Names the values of one enum together, so that every value is an entry whatever the order of the values:
 *
 * 1. A value is named by [toEnumValueName]: the name it has always had when that compiles, else its words, where a
 *    character that cannot be in a name is a word break (`aws:kms` is `AwsKms`), and a value without a letter or digit
 *    is spelled out (`<=` is `LessThanOrEqual`, `""` is `Empty`, `" "` is `Blank`).
 * 2. Of values that get one name, those with symbols spell them (`-last_activity` is `MinusLastActivity` next to
 *    `last_activity`, `aws:kms` and `aws-kms` are `AwsColonKms` and `AwsMinusKms`); a value without symbols keeps its name.
 * 3. Of what still has one name, the first declared keeps it and each later value gets the first free number from 2.
 */
internal fun enumEntryNames(wireValues: List<String>): EnumEntryNames {
    val values = wireValues.distinct()
    val plain = values.map { it.toEnumEntryName() }
    val names = plain.toMutableList()
    val reasons = arrayOfNulls<EnumRenameReason>(values.size)

    for (group in values.indices.groupBy { plain[it] }.values.filter { it.size > 1 }) {
        for (index in group) {
            if (values[index].none(Char::isEnumSymbol)) continue
            val spelled = values[index].toSpelledEntryName()
            if (spelled != names[index]) {
                names[index] = spelled
                reasons[index] = EnumRenameReason.SpelledSymbols
            }
        }
    }

    val taken = names.mapTo(mutableSetOf()) { it }
    val seen = mutableSetOf<String>()
    for (index in values.indices) {
        if (seen.add(names[index])) continue
        val base = names[index]
        val numbered = generateSequence(2) { it + 1 }.map { "$base$it" }.first { it !in taken }
        names[index] = numbered
        reasons[index] = EnumRenameReason.Numbered
        taken += numbered
        seen += numbered
    }

    val finalNames = names.map { it.asEntryName() }
    val renames = values.indices.mapNotNull { index ->
        reasons[index]?.let { EnumEntryRename(values[index], finalNames[index], plain[index].asEntryName(), it) }
    }
    return EnumEntryNames(values.zip(finalNames).toMap(), renames)
}

/**
 * The name of a value of an enum alone: what it has always been when that compiles, else a name built from the words
 * of the value, and for a value without a letter or digit the spelled-out symbols. Never empty, never a name that
 * Kotlin rejects.
 */
fun toEnumValueName(rawValue: String): String = rawValue.toEnumEntryName().asEntryName()

private fun String.toEnumEntryName(): String {
    if (none(Char::isLetterOrDigit)) return toSymbolEntryName()
    val legacy = legacyEnumValueName(this)
    if (legacy.compilesAsEntryName()) return legacy.unescapeBackticks()
    return toEntryWords(spellSymbols = false).ifEmpty { toSymbolEntryName() }
}

private fun String.toSpelledEntryName(): String =
    toEntryWords(spellSymbols = true).ifEmpty { toEnumEntryName() }

/** A value without letter or digit: its symbols spelled out, `Empty` for no characters, `Blank` for only whitespace. */
private fun String.toSymbolEntryName(): String = when {
    isEmpty() -> "Empty"
    isBlank() -> "Blank"
    else -> toEntryWords(spellSymbols = true).ifEmpty { "Underscore" }
}

/** `name` as written in code: in backticks when it is not an identifier (a leading digit, for one). */
private fun String.asEntryName(): String = if (isValidClassname()) this else "`$this`"

/** The characters a name in backticks cannot have on the JVM, and whitespace, which the name would not survive. */
private const val IllegalInBackticks = ".;[]/<>:\\`"

private fun String.compilesAsEntryName(): Boolean {
    if (isValidClassname()) return true
    if (length < 3 || !startsWith("`") || !endsWith("`")) return false
    return substring(1, length - 1).none { it in IllegalInBackticks || it.isWhitespace() || it.isISOControl() }
}

/** A character that is not part of a word: it is a word break, and the symbols that tell two values apart. */
internal fun Char.isEnumSymbol(): Boolean = !isLetterOrDigit() && this != '_' && !isWhitespace()

/** What the symbols of an enum value are spelled as; longer symbols first. */
private val SpelledEntrySymbols = listOf(
    "<=" to "LessThanOrEqual",
    ">=" to "GreaterThanOrEqual",
    "!=" to "NotEquals",
    "<" to "LessThan",
    ">" to "GreaterThan",
    "=" to "Equals",
    "!" to "Exclamation",
    "+" to "Plus",
    "-" to "Minus",
    "." to "Dot",
    "*" to "Star",
    "/" to "Slash",
    "\\" to "Backslash",
    ":" to "Colon",
    ";" to "Semicolon",
    "," to "Comma",
    "@" to "At",
    "#" to "Hash",
    "$" to "Dollar",
    "%" to "Percent",
    "&" to "Ampersand",
    "(" to "OpenParen",
    ")" to "CloseParen",
    "[" to "OpenBracket",
    "]" to "CloseBracket",
    "{" to "OpenBrace",
    "}" to "CloseBrace",
    "|" to "Pipe",
    "?" to "Question",
    "'" to "Quote",
    "\"" to "DoubleQuote",
    "~" to "Tilde",
    "^" to "Caret",
    "`" to "Backtick",
)

/**
 * The words of this value in PascalCase: each run of letters and digits is a word, `_`, whitespace and (unless
 * [spellSymbols]) every other character separate the words. A symbol that is not in [SpelledEntrySymbols] is spelled
 * as `U` and its code point in four hex digits (`U20AC`).
 */
private fun String.toEntryWords(spellSymbols: Boolean): String = buildString {
    var index = 0
    while (index < this@toEntryWords.length) {
        val char = this@toEntryWords[index]
        when {
            char.isLetterOrDigit() -> {
                val end = (index until this@toEntryWords.length)
                    .firstOrNull { !this@toEntryWords[it].isLetterOrDigit() } ?: this@toEntryWords.length
                append(this@toEntryWords.substring(index, end).toPascalCase())
                index = end
            }

            char.isEnumSymbol() && spellSymbols -> {
                val symbol = SpelledEntrySymbols.firstOrNull { it.first.let { text -> this@toEntryWords.startsWith(text, index) } }
                if (symbol != null) {
                    append(symbol.second)
                    index += symbol.first.length
                } else {
                    append("U" + char.code.toString(16).uppercase().padStart(4, '0'))
                    index++
                }
            }

            else -> index++
        }
    }
}
