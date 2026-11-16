package io.github.nomisrev.openapi

/** The name of the client's root class, and why it is not the title as it is ([ApiTree.rootClassName]). */
internal data class RootClassName(val simpleName: String, val reason: String?)

/**
 * The name of the root class of the client: [RenderConfig.clientName] as it is when it is set (and can be a class
 * name, see [rootClassNameDiagnostics]), otherwise from the title of the document.
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
internal fun ApiTree.rootClassName(config: RenderConfig): RootClassName {
    val taken = firstSegmentClassNames()
    val configured = config.clientName
    return if (configured != null && configured.isClientName() && configured.lowercase() !in taken) {
        RootClassName(configured, null)
    } else derivedRootClassName(taken)
}

/** The lower-cased names of the classes of the first path segments, which are files in the root class's package. */
private fun ApiTree.firstSegmentClassNames(): Set<String> =
    children.uniqueNames(operations.keys).mapTo(mutableSetOf()) { it.simpleName.lowercase() }

/** A name a user can give the root class: a Kotlin class name without back-ticks. */
private fun String.isClientName(): Boolean = matches(ClientNameRegex) && this !in KOTLIN_KEYWORDS

private val ClientNameRegex = Regex("[A-Za-z_][A-Za-z0-9_]*")

private fun ApiTree.derivedRootClassName(taken: Set<String>): RootClassName {
    val plain = name.toPascalCase()
    val words = name.map { if (it.isLetterOrDigit()) it else ' ' }.joinToString("").toPascalCase()
    val identifier = when {
        words.isEmpty() -> "Api"
        words.first().isDigit() -> "Api$words"
        else -> words
    }
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

/**
 * A warning when the root class is not named with the title as it is, or an error when the configured
 * [RenderConfig.clientName] cannot be its name. The client is then generated under the name from the title, so the
 * error is not followed by others about a class that does not exist.
 */
internal fun ApiTree.rootClassNameDiagnostics(config: RenderConfig): List<GenerationDiagnostic> {
    config.clientName?.let { configured ->
        val reason = when {
            !configured.isClientName() ->
                "is not a Kotlin class name: use letters, digits and '_', not starting with a digit, and no keyword."
            configured.lowercase() in firstSegmentClassNames() ->
                "is the name of the class of a first path segment, which would be the same file."
            else -> return emptyList()
        }
        return listOf(
            GenerationDiagnostic(GenerationDiagnosticSeverity.Error, "The client name '$configured' $reason")
        )
    }
    val root = rootClassName(config)
    val reason = root.reason ?: return emptyList()
    return listOf(
        GenerationDiagnostic(
            GenerationDiagnosticSeverity.Warning,
            "The client class is generated as '${root.simpleName}': $reason",
        )
    )
}
