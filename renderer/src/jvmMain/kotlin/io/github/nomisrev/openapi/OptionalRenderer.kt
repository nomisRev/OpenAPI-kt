package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.NOTHING
import io.github.nomisrev.openapi.routes.Route

/*
 * A property that is not `required` and is nullable has three states on the wire: absent, `null`, a value. It is
 * generated as `Optional<T>` (`Absent`, `Null`, `Value`), and only such a property. The other combinations are
 * `T? = null` (optional, not nullable: `null` is never valid, so it is omitted), `T?` (required, nullable) and `T`.
 */

private const val DefaultOptionalTypeName = "Optional"
private const val OptionalSerializerName = "OptionalSerializer"
private val KSerializerType = ClassName("kotlinx.serialization", "KSerializer")
private val SerializableType = ClassName("kotlinx.serialization", "Serializable")
private val SerializationExceptionType = ClassName("kotlinx.serialization", "SerializationException")
private val SerialDescriptorType = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
private val NullableDescriptorMember = MemberName("kotlinx.serialization.descriptors", "nullable")
private val DecoderType = ClassName("kotlinx.serialization.encoding", "Decoder")
private val EncoderType = ClassName("kotlinx.serialization.encoding", "Encoder")
private val ExperimentalSerializationApiType = ClassName("kotlinx.serialization", "ExperimentalSerializationApi")

/** Whether this property has the three states absent, `null` and a value: it is optional and nullable. */
internal val Model.Object.Property.isOptionalNullable: Boolean
    get() = !isRequired && model.isNullable

/** The simple name of the generated tri-state type; [RenderConfig.optionalTypeName] when the tree chose one. */
internal val RenderConfig.optionalName: String
    get() = optionalTypeName ?: DefaultOptionalTypeName

/**
 * The generated tri-state type. There is one, in the model package, also for the inline models and the `invoke`
 * parameters of the api package ([packageName]): two copies would be two types a caller cannot pass for one another.
 */
@Suppress("UNUSED_PARAMETER")
internal fun RenderConfig.optionalClassName(packageName: String): ClassName =
    ClassName(modelPackage.toPackageName(), optionalName)

/** `Optional<T>` for the (non-null) value type [valueType]. */
internal fun RenderConfig.optionalOf(packageName: String, valueType: TypeName): TypeName =
    optionalClassName(packageName).parameterizedBy(valueType.copy(nullable = false))

/** `Optional.Absent`, the Kotlin default of an optional and nullable property. */
internal fun RenderConfig.optionalAbsent(packageName: String): CodeBlock =
    CodeBlock.of("%T.Absent", optionalClassName(packageName))

/**
 * The first of `Optional`, `OptionalProperty`, `OptionalValue`... that no top-level class of the generated code
 * already has, so the support file never replaces or shadows a model, a path or the client root.
 */
internal fun ApiTree.chooseOptionalTypeName(config: RenderConfig): String {
    val taken = buildSet {
        models.forEach { model ->
            (model as? Model.ContextHolder)?.context?.toClassName(config)?.let { add(it.topLevelClassName().simpleName) }
        }
        add(name.toPascalCase())
        children.zip(children.uniqueNames()).forEach { [_, childName] -> add(childName.simpleName) }
    }
    return generateSequence(0) { it + 1 }
        .map { if (it == 0) DefaultOptionalTypeName else "${DefaultOptionalTypeName}Property${if (it == 1) "" else it}" }
        .first { it !in taken }
}

internal fun RenderConfig.withOptionalTypeName(tree: ApiTree): RenderConfig =
    if (optionalTypeName != null) this else copy(optionalTypeName = tree.chooseOptionalTypeName(this))

/** The packages whose generated classes have an optional and nullable property, found in this model. */
internal fun Model.collectOptionalPackages(config: RenderConfig, into: MutableSet<String>) {
    when (this) {
        is Model.Object -> {
            if (properties.values.any { it.isOptionalNullable }) {
                into += context.toClassName(config).packageName
            }
            properties.values.forEach { it.model.collectOptionalPackages(config, into) }
            (additionalProperties as? Model.Object.AdditionalProperties.Schema)
                ?.value
                ?.collectOptionalPackages(config, into)
        }

        is Model.Collection -> inner.collectOptionalPackages(config, into)
        is Model.Dictionary -> inner.collectOptionalPackages(config, into)
        is Model.Union -> cases.forEach { it.model.collectOptionalPackages(config, into) }
        is Model.DiscriminatedObject -> subtypes.forEach { it.collectOptionalPackages(config, into) }
        is Model.Enum,
        is Model.Reference,
        is Model.ByteArray,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Primitive -> Unit
    }
}

/** The packages of the inline models of the operations of this tree whose classes have such a property. */
internal fun ApiTree.collectOptionalPackages(config: RenderConfig, into: MutableSet<String>) {
    operations.values.forEach { it.collectOptionalPackages(config, into) }
    children.forEach { it.collectOptionalPackages(config, into) }
}

private fun PathNode.collectOptionalPackages(config: RenderConfig, into: MutableSet<String>) {
    operations.values.forEach { it.collectOptionalPackages(config, into) }
    children.forEach { it.collectOptionalPackages(config, into) }
}

private fun Route.collectOptionalPackages(config: RenderConfig, into: MutableSet<String>) {
    parameters.forEach { it.type.collectOptionalPackages(config, into) }
    body?.types.orEmpty().values.forEach { body ->
        when (body) {
            is Route.Body.SetBody -> body.type.collectOptionalPackages(config, into)
            is Route.Body.OverloadedBody -> body.cases.forEach { it.model.collectOptionalPackages(config, into) }
            is Route.Body.FormUrlEncoded -> body.parameters.forEach { it.type.collectOptionalPackages(config, into) }
            is Route.Body.Multipart.Value -> body.parameters.forEach { it.type.collectOptionalPackages(config, into) }
            is Route.Body.Multipart.Ref -> body.value.collectOptionalPackages(config, into)
        }
    }
    (returns.responses.values + returns.responseRanges.values + listOfNotNull(returns.default)).forEach { returnType ->
        returnType.types.values.forEach { it.collectOptionalPackages(config, into) }
    }
}

private fun ClassName.topLevelClassName(): ClassName = enclosingClassName()?.topLevelClassName() ?: this

/**
 * The support file of [packageName]: the tri-state `Optional` and its serializer. There is no runtime library, so it
 * is generated next to the models that use it, like `AttemptDeserialize.kt`.
 *
 * ```kotlin
 * @Serializable(with = OptionalSerializer::class)
 * public sealed interface Optional<out T> {
 *   public data object Absent : Optional<Nothing>
 *   public data object Null : Optional<Nothing>
 *   public data class Value<out T>(public val value: T) : Optional<T>
 * }
 * ```
 *
 * `Absent` is never encoded: the property is `@EncodeDefault(NEVER)`, so its key is left out, and a missing key
 * decodes to the default `Absent`. `Null` is `null` on the wire and `Value` is the value, whatever `explicitNulls`
 * and `encodeDefaults` of the `Json` are, because the property's type is not nullable: kotlinx.serialization only
 * drops a `null` it finds in a nullable property.
 */
internal fun generateOptionalSupport(config: RenderConfig, packageName: String): FileSpec {
    val optional = config.optionalClassName(packageName)
    val serializer = ClassName(packageName, OptionalSerializerName)
    val t = TypeVariableName("T", KModifier.OUT)
    val tInvariant = TypeVariableName("T")

    val optionalType = TypeSpec.interfaceBuilder(optional)
        .addModifiers(KModifier.PUBLIC, KModifier.SEALED)
        .addTypeVariable(t)
        .addAnnotation(AnnotationSpec.builder(SerializableType).addMember("with = %T::class", serializer).build())
        .addKdoc(
            "A property that is optional and nullable has three states on the wire: [Absent] (no key), [Null] " +
                "(`null`) and [Value].\n"
        )
        .addProperty(
            PropertySpec.builder("valueOrNull", t.copy(nullable = true))
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return (this as? %T)?.value", optional.nestedClass("Value").parameterizedBy(t))
                        .build()
                )
                .addKdoc("The value, or `null` for [Absent] and [Null].\n")
                .build()
        )
        .addType(
            TypeSpec.objectBuilder("Absent")
                .addModifiers(KModifier.PUBLIC, KModifier.DATA)
                .addSuperinterface(optional.parameterizedBy(NOTHING))
                .build()
        )
        .addType(
            TypeSpec.objectBuilder("Null")
                .addModifiers(KModifier.PUBLIC, KModifier.DATA)
                .addSuperinterface(optional.parameterizedBy(NOTHING))
                .build()
        )
        .addType(
            TypeSpec.classBuilder("Value")
                .addModifiers(KModifier.PUBLIC, KModifier.DATA)
                .addTypeVariable(t)
                .primaryConstructor(FunSpec.constructorBuilder().addParameter("value", t).build())
                .addProperty(PropertySpec.builder("value", t).initializer("value").build())
                .addSuperinterface(optional.parameterizedBy(t))
                .build()
        )
        .build()

    val optionalOfT = optional.parameterizedBy(tInvariant)
    val serializerType = TypeSpec.classBuilder(serializer)
        .addModifiers(KModifier.PUBLIC)
        .addTypeVariable(tInvariant)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("valueSerializer", KSerializerType.parameterizedBy(tInvariant))
                .build()
        )
        .addSuperinterface(KSerializerType.parameterizedBy(optionalOfT))
        .addProperty(
            PropertySpec.builder("valueSerializer", KSerializerType.parameterizedBy(tInvariant), KModifier.PRIVATE)
                .initializer("valueSerializer")
                .build()
        )
        .addProperty(
            // Nullable, so a `null` is never taken for an unset non-nullable element (`coerceInputValues`).
            PropertySpec.builder("descriptor", SerialDescriptorType, KModifier.OVERRIDE)
                .addAnnotation(
                    AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                        .addMember("%T::class", ExperimentalSerializationApiType)
                        .build()
                )
                .initializer("valueSerializer.descriptor.%M", NullableDescriptorMember)
                .build()
        )
        .addFunction(
            FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", EncoderType)
                .addParameter("value", optionalOfT)
                .beginControlFlow("when (value)")
                .addStatement(
                    "%T.Absent -> throw %T(%S)",
                    optional,
                    SerializationExceptionType,
                    "${optional.simpleName}.Absent has no wire form: it is only valid as the value of an " +
                        "@EncodeDefault(NEVER) property, which leaves the key out",
                )
                .addStatement("%T.Null -> encoder.encodeNull()", optional)
                .addStatement(
                    "is %T -> encoder.encodeSerializableValue(valueSerializer, value.value)",
                    optional.nestedClass("Value"),
                )
                .endControlFlow()
                .build()
        )
        .addFunction(
            FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("decoder", DecoderType)
                .returns(optionalOfT)
                .beginControlFlow("return if (decoder.decodeNotNullMark())")
                .addStatement(
                    "%T(decoder.decodeSerializableValue(valueSerializer))",
                    optional.nestedClass("Value"),
                )
                .nextControlFlow("else")
                .addStatement("decoder.decodeNull()")
                .addStatement("%T.Null", optional)
                .endControlFlow()
                .build()
        )
        .build()

    return FileSpec.builder(packageName, optional.simpleName)
        .addType(optionalType)
        .addType(serializerType)
        .build()
}

/**
 * The Kotlin expression `expr.valueOrNull` of the optional and nullable property [expr]: its value, or `null` when it
 * is `Absent` or `Null`. The wire formats that have no null (urlencoded form, query, header, cookie) leave both out.
 */
internal fun optionalValueOrNull(expr: String): String = "$expr.valueOrNull"
