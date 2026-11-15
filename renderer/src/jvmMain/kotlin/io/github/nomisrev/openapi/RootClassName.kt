package io.github.nomisrev.openapi

/** The name of the client's root class, and why it is not the title as it is ([ApiTree.rootClassName]). */
internal data class RootClassName(val simpleName: String, val reason: String?)

/**
 * The name of the root class of the client, from the title of the document.
 *
 * A character that is not a letter or a digit is a word break, so the name is an identifier without back-ticks
 * (`CS:GO v3 Scores` is `CSGOV3Scores`, a `:` is not legal in a class name at all). A title without a letter or a
 * digit is `Api`, and one that starts with a digit is prefixed with `Api` (`Api1PasswordConnect`).
 *
 * The classes of the first path segments are in the same package, a file each. A title with the name of one of them
 * (`Files` and `/files`, or `APOD` and `/apod`, which are one file on a case-insensitive file system) would replace
 * it, or be replaced by it, so the root class is then named with `Api` after it (`FilesApi`), or `Client` when that is
 * taken too.
 */
internal fun ApiTree.rootClassName(): RootClassName {
    val plain = name.toPascalCase()
    val words = name.map { if (it.isLetterOrDigit()) it else ' ' }.joinToString("").toPascalCase()
    val identifier = when {
        words.isEmpty() -> "Api"
        words.first().isDigit() -> "Api$words"
        else -> words
    }
    val taken = children.uniqueNames(operations.keys).mapTo(mutableSetOf()) { it.simpleName.lowercase() }
    val unique = sequenceOf(identifier, "${identifier}Api", "${identifier}Client")
        .plus(generateSequence(2) { it + 1 }.map { "$identifier$it" })
        .first { it.lowercase() !in taken }
    val reason = when {
        unique != identifier ->
            "a first path segment is generated as the class '${
                children.zip(children.uniqueNames(operations.keys))
                    .first { it.second.simpleName.equals(identifier, ignoreCase = true) }.second.simpleName
            }', which would be the same file."
        identifier != plain && plain.isEmpty() -> "the document has no title to name it from."
        identifier != plain -> "the title '$name' is not a Kotlin name."
        else -> null
    }
    return RootClassName(unique, reason)
}

/** A warning when the root class is not named with the title as it is. */
internal fun ApiTree.rootClassNameDiagnostics(): List<GenerationDiagnostic> {
    val root = rootClassName()
    val reason = root.reason ?: return emptyList()
    return listOf(
        GenerationDiagnostic(
            GenerationDiagnosticSeverity.Warning,
            "The client class is generated as '${root.simpleName}': $reason",
        )
    )
}
