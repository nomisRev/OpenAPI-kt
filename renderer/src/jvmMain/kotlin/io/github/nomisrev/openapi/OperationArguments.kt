package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route

/**
 * An argument of an `invoke` function that is not a parameter of the operation: the request body, what chooses among
 * bodies, and the properties or fields a body is flattened into.
 */
internal sealed interface BodyArgument {
    /** A property of a flattened inline JSON body, or a form or multipart field: named after its [wireName]. */
    data class Field(val wireName: String) : BodyArgument

    /** The request body, `body`, then `requestBody`. */
    data object Payload : BodyArgument

    /** What chooses among request bodies of one Kotlin signature, `requestType`, then `bodyRequestType`. */
    data object RequestType : BodyArgument

    /** The media type the caller supplies, `contentType`, then `bodyContentType`. */
    data object MediaType : BodyArgument

    /** The block in which the caller sets the body, `block`, then `requestBlock`. */
    data object RequestBlock : BodyArgument

    /** The additional properties of a flattened body, `additional`, then `bodyAdditional`. */
    data object Additional : BodyArgument
}

/** Why an argument is not named after what it was declared as. */
internal enum class RenameReason(val explanation: String) {
    SpelledSymbols("another parameter has the same Kotlin name, so the symbols of this one are spelled out."),
    Location("another parameter in a different location has the same Kotlin name, so the location is appended."),
    BodyPrefix("a parameter has the same Kotlin name, so the request body's prefix is added."),
    Numbered("an earlier argument has the same Kotlin name, so the first free number from 2 is appended."),
    GeneratedName("another argument has the name the generated code gives it."),
}

/** An argument whose Kotlin name is not the one its wire name gives it. */
internal data class ArgumentRename(
    /** `parameter 'ValidUntilDate<' (in query)`, `request body property 'ids'`. */
    val subject: String,
    val name: String,
    val reason: RenameReason,
)

/**
 * The Kotlin names of every input of one overload of an operation: its parameters, the properties of a flattened body
 * or the fields of a form, `body`, `requestType`, `contentType` and the caller's block ([operationArguments]). The
 * signature of the function and each piece of generated code that reads an argument ask this one result, so that they
 * cannot disagree. Names are as written in generated code: back-ticked when they are not an identifier.
 */
internal class OperationArguments(
    private val parameterNames: List<Pair<Route.Input, String>>,
    private val fieldNames: Map<String, String>,
    private val generatedNames: Map<BodyArgument, String>,
    val renames: List<ArgumentRename>,
) {
    fun parameter(input: Route.Input): String =
        parameterNames.firstOrNull { it.first === input }?.second ?: error("${input.name} is not an argument")

    fun field(wireName: String): String = fieldNames[wireName] ?: error("$wireName is not an argument")

    val body: String get() = generated(BodyArgument.Payload)
    val requestType: String get() = generated(BodyArgument.RequestType)
    val contentType: String get() = generated(BodyArgument.MediaType)
    val block: String get() = generated(BodyArgument.RequestBlock)
    val additional: String get() = generated(BodyArgument.Additional)

    /** The name of [argument], or the one it is given when nothing else has it, for code of an overload without it. */
    private fun generated(argument: BodyArgument): String =
        generatedNames[argument] ?: argument.preferredName()
}

/** The symbols of a parameter name that are spelled out when two parameters have one Kotlin name. */
private val SpelledSymbols = listOf(
    "<=" to "Lte",
    ">=" to "Gte",
    "<" to "Lt",
    ">" to "Gt",
    "[]" to "List",
    "!" to "Not",
)

/** The words the symbols of this parameter name are spelled as, `null` when it has none: `ValidUntilDate<` is `ValidUntilDateLt`. */
internal fun String.spellSymbolsOrNull(): String? {
    if (SpelledSymbols.none { [symbol, _] -> symbol in this }) return null
    return SpelledSymbols.fold(this) { text, [symbol, word] -> text.replace(symbol, " $word ") }
}

private sealed class Slot(val wanted: String) {
    var name: String = wanted
    var reason: RenameReason? = null

    fun rename(to: String, because: RenameReason) {
        name = to
        reason = because
    }

    class Param(val input: Route.Input) : Slot(input.name.toParamName().unescapeBackticks())
    class Field(val wireName: String) : Slot(wireName.toParamName().unescapeBackticks())
}

private fun String.escaped(): String = if (isValidParamName()) this else "`$this`"

private fun String.upperFirst(): String = replaceFirstChar(Char::uppercase)

/**
 * The names of the arguments of an overload of this operation, which takes the non-path parameters and [body]. Two
 * inputs that want one Kotlin name are told apart in this order, whatever the order of the spec:
 *
 * 1. A property or field of the body that has the name of a parameter gets the prefix `body` (`ids` is `bodyIds`): the
 *    parameter keeps its name.
 * 2. Of parameters that have one Kotlin name, those with symbols that can be spelled ([SpelledSymbols]) are spelled
 *    (`ValidUntilDate<` is `validUntilDateLt`), the others keep their name.
 * 3. Parameters with one Kotlin name in different locations all get the location (`idQuery`, `idHeader`).
 * 4. The first of what still has one name keeps it; each later input gets the first free number from 2, as
 *    [uniqueNames] does for path segments.
 *
 * (Applied as 2, 3, 1, 4: the parameters are told apart first, then the body's names are checked against theirs.) The
 * arguments the generated code adds are named last, and avoid every other argument.
 */
internal fun Route.operationArguments(body: List<BodyArgument> = emptyList()): OperationArguments {
    val params = parameters.filter { it.input != Parameter.Input.Path }.map { Slot.Param(it) }
    val fields = body.filterIsInstance<BodyArgument.Field>().map { Slot.Field(it.wireName) }

    // Spell the symbols of parameters that would collide.
    for (group in params.groupBy { it.name }.values.filter { it.size > 1 }) {
        for (slot in group) {
            val spelled = slot.input.name.spellSymbolsOrNull()?.toParamName()?.unescapeBackticks() ?: continue
            slot.rename(spelled, RenameReason.SpelledSymbols)
        }
    }
    // Parameters of one name in different locations all take their location.
    for (group in params.groupBy { it.name }.values.filter { group -> group.map { it.input.input }.distinct().size > 1 }) {
        for (slot in group) {
            slot.rename(slot.name + slot.input.input.value.upperFirst(), RenameReason.Location)
        }
    }
    // A body property or field that has the name of a parameter.
    val parameterNames = params.mapTo(mutableSetOf()) { it.name }
    for (slot in fields) {
        if (slot.name in parameterNames) slot.rename("body" + slot.name.upperFirst(), RenameReason.BodyPrefix)
    }
    // What is left: the first keeps the name.
    val all = params + fields
    val taken = all.mapTo(mutableSetOf()) { it.name }
    val seen = mutableSetOf<String>()
    for (slot in all) {
        if (seen.add(slot.name)) continue
        val numbered = generateSequence(2) { it + 1 }.map { "${slot.name}$it" }.first { it !in taken }
        slot.rename(numbered, RenameReason.Numbered)
        taken += numbered
        seen += numbered
    }

    val generated = body.filter { it !is BodyArgument.Field }.associateWith { argument ->
        val first = argument.preferredName()
        val second = argument.fallbackName()
        val candidates = sequenceOf(first, second) + generateSequence(2) { it + 1 }.map { "$first$it" }
        val name = candidates.first { it !in taken }
        taken += name
        name to if (name == first) null else RenameReason.GeneratedName
    }

    val renames = params.mapNotNull { slot ->
        slot.reason?.let { ArgumentRename("parameter '${slot.input.name}' (in ${slot.input.input.value})", slot.name, it) }
    } + fields.mapNotNull { slot ->
        slot.reason?.let { ArgumentRename("request body property '${slot.wireName}'", slot.name, it) }
    } + generated.mapNotNull { [argument, named] ->
        named.second?.let { ArgumentRename(argument.describe(), named.first, it) }
    }
    return OperationArguments(
        parameterNames = params.map { it.input to it.name.escaped() },
        fieldNames = fields.associate { it.wireName to it.name.escaped() },
        generatedNames = generated.mapValues { it.value.first.escaped() },
        renames = renames,
    )
}

private fun BodyArgument.preferredName(): String = when (this) {
    BodyArgument.Payload -> "body"
    BodyArgument.RequestType -> "requestType"
    BodyArgument.MediaType -> "contentType"
    BodyArgument.RequestBlock -> "block"
    BodyArgument.Additional -> "additional"
    is BodyArgument.Field -> error("A field is named from its wire name")
}

private fun BodyArgument.fallbackName(): String = when (this) {
    BodyArgument.Payload -> "requestBody"
    BodyArgument.RequestType -> "bodyRequestType"
    BodyArgument.MediaType -> "bodyContentType"
    BodyArgument.RequestBlock -> "requestBlock"
    BodyArgument.Additional -> "bodyAdditional"
    is BodyArgument.Field -> error("A field is named from its wire name")
}

private fun BodyArgument.describe(): String = when (this) {
    BodyArgument.Payload -> "the request body"
    BodyArgument.RequestType -> "the request type"
    BodyArgument.MediaType -> "the media type of the request body"
    BodyArgument.RequestBlock -> "the request builder block"
    BodyArgument.Additional -> "the additional properties of the request body"
    is BodyArgument.Field -> "a request body property"
}

/**
 * The arguments of each overload of this operation that has a request body of its own (see
 * `addOperationInvokeFunctionsForContentType`), for the diagnostics to report what the renderer names.
 */
internal fun Route.overloadArguments(config: RenderConfig): List<OperationArguments> {
    val bodies = body ?: return listOf(operationArguments())
    val strategy = if (bodies.variants().size > 1) bodies.detectSignatureClashes(config) else null
    fun overload(selectedBody: Route.Body?, requestType: Boolean = false) =
        operationArguments(bodyArguments(config, selectedBody, requestType))
    return when {
        strategy is RequestBodyStrategy.ClashingWithEnum ->
            listOf(overload(strategy.clashing.first().body, requestType = true)) +
                strategy.unique.map { overload(it.body) }

        strategy is RequestBodyStrategy.SeparateOverloads -> strategy.variants.map { overload(it.body) }
        strategy is RequestBodyStrategy.Single -> listOf(overload(strategy.variant.body))
        else -> listOf(
            flattenedBodyArgumentsOrNull()?.let { operationArguments(it) } ?: overload(bodies.defaultOrNull())
        )
    }
}
