package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.joinToCode

private val JsonPrimitiveType = ClassName("kotlinx.serialization.json", "JsonPrimitive")
private val JsonNullType = ClassName("kotlinx.serialization.json", "JsonNull")
private val JsonArrayType = ClassName("kotlinx.serialization.json", "JsonArray")
private val JsonObjectType = ClassName("kotlinx.serialization.json", "JsonObject")
private val DoubleOrNullMember = MemberName("kotlinx.serialization.json", "doubleOrNull")
private val BooleanOrNullMember = MemberName("kotlinx.serialization.json", "booleanOrNull")

/**
 * The kind of a JSON value, as the decoded [kotlinx.serialization.json.JsonElement] tells it. A union case is only
 * tried for the kinds its schema permits, because kotlinx.serialization's tree decoder does not check the token kind
 * itself: a quoted `"1"` decodes as `Long` and `"true"` as `Boolean` under every `Json`, and with `isLenient` (Ktor's
 * `DefaultJson`) an unquoted `1` or `true` decodes as `String`.
 */
internal enum class JsonTokenKind {
    String,
    Number,
    Boolean,
    Null,
    Array,
    Object;

    val isPrimitive: kotlin.Boolean get() = this != Array && this != Object

    /** A condition on the decoded `value` that holds exactly for this kind, independent of the other conditions. */
    fun condition(): CodeBlock = when (this) {
        String -> CodeBlock.of("value is %T && value.isString", JsonPrimitiveType)
        Number -> CodeBlock.of("value is %T && !value.isString && value.%M != null", JsonPrimitiveType, DoubleOrNullMember)
        Boolean -> CodeBlock.of("value is %T && !value.isString && value.%M != null", JsonPrimitiveType, BooleanOrNullMember)
        Null -> CodeBlock.of("value is %T", JsonNullType)
        Array -> CodeBlock.of("value is %T", JsonArrayType)
        Object -> CodeBlock.of("value is %T", JsonObjectType)
    }
}

private val AnyJsonTokenKind: Set<JsonTokenKind> = JsonTokenKind.entries.toSet()

/**
 * The JSON kinds the serializer the renderer emits for this model (`serializerCode`) accepts. A [Model.Reference] has the kinds of the
 * model it points at; one that is not rendered, or that refers back to a model being resolved, adds no kinds of its
 * own, so `A: oneOf [string, array of A, $ref A]` accepts strings and arrays.
 */
internal fun Model.jsonTokenKinds(
    config: RenderConfig,
    resolving: Set<NamingContext> = emptySet(),
): Set<JsonTokenKind> {
    fun Model.kinds(): Set<JsonTokenKind> = jsonTokenKinds(config, resolving)

    val kinds = when (this) {
        is Model.Primitive.String,
        is Model.Uuid,
        is Model.Date,
        is Model.DateTime -> setOf(JsonTokenKind.String)

        is Model.Primitive.Int,
        is Model.Primitive.Long,
        is Model.Primitive.Float,
        is Model.Primitive.Double -> setOf(JsonTokenKind.Number)

        is Model.Primitive.Boolean -> setOf(JsonTokenKind.Boolean)
        // `Unit.serializer()` reads `{}`.
        is Model.Primitive.Unit -> setOf(JsonTokenKind.Object)
        // `ByteArraySerializer()` reads an array of numbers.
        is Model.ByteArray,
        is Model.Collection -> setOf(JsonTokenKind.Array)

        is Model.Dictionary,
        is Model.DiscriminatedObject -> setOf(JsonTokenKind.Object)

        is Model.Object -> transparentWrappedValueOrNull()?.kinds() ?: setOf(JsonTokenKind.Object)
        is Model.Enum -> inner.kinds()
        is Model.Union -> cases.flatMapTo(mutableSetOf()) { it.model.kinds() }
        is Model.FreeFormJson -> AnyJsonTokenKind
        // `Target.serializer()` reads what the target's class reads; a nullable target's class is not nullable itself.
        is Model.Reference -> when (val target = config.resolve(this)) {
            null -> AnyJsonTokenKind
            else ->
                if (context in resolving) emptySet()
                else target.with(isNullable = false).jsonTokenKinds(config, resolving + context)
        }
    }
    return if (isNullable) kinds + JsonTokenKind.Null else kinds
}

/** The bare value a synthetic wrapper around a non-object schema renders as (a `value class`), or `null`. */
internal fun Model.Object.transparentWrappedValueOrNull(): Model? =
    properties.values.singleOrNull()?.model
        ?.takeIf { isTransparentWrapper && additionalProperties == Model.Object.AdditionalProperties.False }

internal class TokenKindBranch<C>(val kinds: List<JsonTokenKind>, val cases: List<C>) {
    fun condition(): CodeBlock =
        if (kinds.size == 1) kinds.single().condition()
        else kinds.map { kind ->
            if (kind.isPrimitive && kind != JsonTokenKind.Null) CodeBlock.of("(%L)", kind.condition()) else kind.condition()
        }.joinToCode(" || ")
}

/**
 * Splits [this] cases, in their deserialization order, by the JSON kind of the value: one branch per group of kinds
 * that share candidates, and [TokenKindDispatch.otherwise] for the cases that accept any value (free-form JSON), which
 * also decode the kinds no other case accepts. `null` when no case accepts a primitive: object and array serializers
 * reject every other kind under every `Json` configuration, so trying the cases in order is already exact.
 */
internal fun <C> List<C>.tokenKindDispatchOrNull(kindsOf: (C) -> Set<JsonTokenKind>): TokenKindDispatch<C>? {
    val kindsByCase = map { it to kindsOf(it) }
    if (kindsByCase.none { [_, kinds] -> kinds.any(JsonTokenKind::isPrimitive) }) return null

    val otherwise = kindsByCase.filter { [_, kinds] -> kinds == AnyJsonTokenKind }.map { it.first }
    val branches = JsonTokenKind.entries
        .map { kind -> kind to kindsByCase.filter { [_, kinds] -> kind in kinds }.map { it.first } }
        .filter { [_, cases] -> cases != otherwise }
        .groupBy(keySelector = { it.second }, valueTransform = { it.first })
        .map { [cases, kinds] -> TokenKindBranch(kinds, cases) }
    return if (branches.isEmpty()) null else TokenKindDispatch(branches, otherwise)
}

internal class TokenKindDispatch<C>(val branches: List<TokenKindBranch<C>>, val otherwise: List<C>)
