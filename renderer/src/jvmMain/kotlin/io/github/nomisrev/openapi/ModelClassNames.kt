package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.routes.SchemaContext

/**
 * The top-level classes of the support files that [generateModels] puts into the model package. A schema never takes
 * these names: it is numbered instead. The tri-state type has the other direction: it takes the first of `Optional`,
 * `OptionalProperty`... that no entry of [RenderConfig.modelClassNames] has ([chooseOptionalTypeName]).
 */
private val SupportClassNames = listOf("AttemptDeserialize", "UnionSerializationException", "OptionalSerializer")

/** The suffix a use of a schema in this context adds to its class name: `Read`, `Write` or none. */
internal fun NamingContext.Reference.classNameSuffix(config: RenderConfig): String =
    if (name in config.contextSpecificNames) {
        when (context) {
            SchemaContext.Read -> "Read"
            SchemaContext.Write -> "Write"
            SchemaContext.Null -> ""
        }
    } else ""

/** The class name a schema is generated as, when it takes its own name: its PascalCase name and the [suffix]. */
internal fun desiredModelClassName(key: String, suffix: String): String = sanitizeSimpleName(key.toPascalCase() + suffix)

/** A class of a schema, or of its `Read` or `Write` shape: [suffix] is `""`, `"Read"` or `"Write"`. */
internal data class ModelClassName(
    val key: String,
    val suffix: String,
    val desired: String,
    val name: String,
    /** What [name] differs from [desired] because of, `null` when it does not. */
    val blocker: Blocker?,
) {
    sealed interface Blocker {
        data object SupportFile : Blocker
        data class Schema(val entry: ModelClassName) : Blocker
    }
}

/**
 * The class name of every schema, and of every `Read` and `Write` shape of a schema, of these models. Each is unique
 * among them, ignoring case (`Users.kt` and `USERS.kt` are one file on a case-insensitive file system), and none is the
 * name of a support file.
 *
 * Schemas are named as they are: `PascalCase(name)`; of those, the one declared first keeps a name more than one has
 * (`_metadata` and `metadata`), the others are numbered with the first free number from 2 (`Metadata2`). The `Read` and
 * `Write` shapes are named after that, so that a schema literally named `AvatarRead` keeps its name and the shape of
 * `Avatar` is the one that is numbered (`AvatarRead2`).
 *
 * Schemas with the same name and the same shape (`__string` and `String`) are one class, and are not numbered.
 */
internal fun List<Model>.modelClassNames(config: RenderConfig): List<ModelClassName> {
    val holders = buildList {
        this@modelClassNames.forEach { model ->
            if (model is Model.ContextHolder && model !is Model.Reference) add(model)
            if (model is Model.DiscriminatedObject) addAll(model.subtypes)
        }
    }
    data class Candidate(val key: String, val suffix: String, val shape: String?)

    val pairs = LinkedHashMap<Pair<String, String>, Candidate>()
    holders.forEach { holder ->
        val root = holder.context.head as? NamingContext.Reference ?: return@forEach
        val id = root.name to root.classNameSuffix(config)
        val topLevel = holder.context.nested.isEmpty()
        val shape = if (topLevel) (holder as Model).contextComparisonKey(config.contextSpecificNames) else null
        val known = pairs[id]
        if (known == null || (known.shape == null && shape != null)) pairs[id] = Candidate(id.first, id.second, shape)
    }

    val taken = SupportClassNames.mapTo(mutableSetOf()) { it.lowercase() }
    val owners = mutableMapOf<String, ModelClassName>()
    val shared = mutableMapOf<Pair<String, String>, ModelClassName>()
    val named = mutableMapOf<Pair<String, String>, ModelClassName>()
    val [plain, variants] = pairs.values.partition { it.suffix.isEmpty() }
    (plain + variants).forEach { pair ->
        val desired = desiredModelClassName(pair.key, pair.suffix)
        val sharedWith = pair.shape?.let { shared[desired.lowercase() to it] }
        val entry = if (sharedWith != null) {
            sharedWith.copy(key = pair.key, suffix = pair.suffix, desired = desired)
        } else {
            val name = if (desired.lowercase() !in taken) desired else {
                generateSequence(2) { it + 1 }.map { "$desired$it" }.first { it.lowercase() !in taken }
            }
            val blocker = if (name == desired) null
            else owners[desired.lowercase()]?.let(ModelClassName.Blocker::Schema) ?: ModelClassName.Blocker.SupportFile
            ModelClassName(pair.key, pair.suffix, desired, name, blocker).also {
                taken += name.lowercase()
                owners[name.lowercase()] = it
                pair.shape?.let { shape -> shared[desired.lowercase() to shape] = it }
            }
        }
        named[pair.key to pair.suffix] = entry
    }
    return pairs.keys.map { named.getValue(it) }
}

/** The class names of the schemas of these models, by schema name and suffix: [RenderConfig.modelClassNames]. */
internal fun RenderConfig.withModelClassNames(models: List<Model>): RenderConfig =
    if (modelClassNames.isNotEmpty()) this
    else copy(modelClassNames = models.modelClassNames(this).associate { (it.key to it.suffix) to it.name })

/**
 * A warning for each schema, or `Read` or `Write` shape of a schema, that is not generated under its own name because
 * another one has it ([modelClassNames]): both schemas, and the reason.
 */
internal fun ApiTree.renamedModelDiagnostics(config: RenderConfig): List<GenerationDiagnostic> =
    models.modelClassNames(config).filter { it.blocker != null }.map { renamed ->
        val reason = when (val blocker = renamed.blocker) {
            is ModelClassName.Blocker.SupportFile -> "'${renamed.desired}' is the name of a generated support file"
            is ModelClassName.Blocker.Schema -> {
                val owner = blocker.entry
                val has = when {
                    owner.name != renamed.desired -> "the Kotlin name '${owner.name}', which differs from it by case only " +
                        "(one file on a case-insensitive file system)"
                    renamed.suffix.isEmpty() -> "the Kotlin name '${owner.name}'"
                    else -> "that name"
                }
                "${owner.describe()} has $has"
            }
            null -> error("Not renamed")
        }
        GenerationDiagnostic(
            GenerationDiagnosticSeverity.Warning,
            "${renamed.describe(capitalized = true)} is generated as '${renamed.name}': $reason"
        )
    }

private fun ModelClassName.describe(capitalized: Boolean = false): String =
    if (suffix.isEmpty()) "${if (capitalized) "Schema" else "schema"} '$key'"
    else "${if (capitalized) "The" else "the"} $suffix shape of schema '$key'"
