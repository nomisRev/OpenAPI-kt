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
 * The root class shares its package with the classes of the first path segments, a file each, and with the support
 * files of the client. A title with the name of one of them (`Files` and `/files`, or `APOD` and `/apod`, which are
 * one file on a case-insensitive file system), or whose server class has one (`Pets` and `/pets-server`), would
 * replace it or not compile next to it ([clash]), so the root class is then named with `Api` after it (`FilesApi`), or
 * `Client` when that is taken too.
 */
internal fun ApiTree.rootClassName(config: RenderConfig): RootClassName {
    val configured = config.clientName
    return if (configured != null && configured.isClientName() && clash(configured) == null) {
        RootClassName(configured, null)
    } else derivedRootClassName()
}

/**
 * Why [candidate] cannot be the root class's name, or `null` when it can: it, or its server class, is a file or a
 * top-level class of the client package already. Names are compared ignoring case, as file names are on a
 * case-insensitive file system.
 */
private fun ApiTree.clash(candidate: String): String? {
    val paths = children.uniqueNames(operations.keys).map { it.simpleName }
    paths.firstOrNull { it.equals(candidate, ignoreCase = true) }?.let { path ->
        return "a first path segment is generated as the class '$path', which would be the same file."
    }
    if (hasServerClass()) {
        paths.firstOrNull { it.equals("${candidate}Server", ignoreCase = true) }?.let { path ->
            return "a first path segment is generated as the class '$path', the name of the client's server class."
        }
    }
    if (candidate.equals(StatusCodesFileName, ignoreCase = true) && buildSet { collectIanaStatusCodes(this) }.isNotEmpty()) {
        return "the names of the status codes are generated in the file '$StatusCodesFileName', which would be the same file."
    }
    if (candidate.equals(SerializationUtilsFileName, ignoreCase = true) && clientNeedsSerializationUtils()) {
        return "the union serialization helpers are generated in the file '$SerializationUtilsFileName', which would " +
            "be the same file."
    }
    return null
}

/** Whether a `<Root>Server` class is generated next to the root class ([serverStrategy]). */
private fun ApiTree.hasServerClass(): Boolean =
    servers.size > 1 || servers.singleOrNull()?.variables?.isNotEmpty() == true

/** A name a user can give the root class: a Kotlin class name without back-ticks. */
private fun String.isClientName(): Boolean = matches(ClientNameRegex) && this !in KOTLIN_KEYWORDS

private val ClientNameRegex = Regex("[A-Za-z_][A-Za-z0-9_]*")

private fun ApiTree.derivedRootClassName(): RootClassName {
    val plain = name.toPascalCase()
    val words = name.map { if (it.isLetterOrDigit()) it else ' ' }.joinToString("").toPascalCase()
    val identifier = when {
        words.isEmpty() -> "Api"
        words.first().isDigit() -> "Api$words"
        else -> words
    }
    val unique = sequenceOf(identifier, "${identifier}Api", "${identifier}Client")
        .plus(generateSequence(2) { it + 1 }.map { "$identifier$it" })
        .first { clash(it) == null }
    val reason = when {
        unique != identifier -> clash(identifier)
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
            else -> clash(configured)?.let { "cannot be used: $it" } ?: return emptyList()
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
