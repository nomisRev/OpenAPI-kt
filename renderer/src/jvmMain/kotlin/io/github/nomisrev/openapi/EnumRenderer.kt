package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Returns true if this enum has at least one entry whose raw wire value differs from its
 * Kotlin entry name, meaning we need a `value: String` constructor parameter so that the
 * wire value can be retrieved without relying on `@SerialName` (which is only respected by
 * kotlinx.serialization, not by Ktor's `parameter()` / form-data `append()`).
 */
fun Model.Enum.needsValueProperty(): Boolean =
    entryNames().let { names ->
        values.any { enumValue ->
            val v = enumValue.wireValue()
            v != names.nameOf(v).unescapeBackticks()
        }
    }

/**
 * How an enum of JSON numbers or booleans goes over the wire. kotlinx.serialization writes a plain `enum class` as a
 * JSON string, so `{"type": "integer", "enum": [300, 600]}` would send `"300"` and refuse the number `300`
 * (OAS 3.1.1 4.8.24: an instance validates only if it is equal to a member, and `300` is not `"300"`).
 */
internal enum class TypedEnumKind(
    val kotlinType: TypeName,
    val primitiveKind: String,
    val encode: String,
    val decode: String,
) {
    Int(INT, "INT", "encodeInt", "decodeInt"),
    Long(LONG, "LONG", "encodeLong", "decodeLong"),
    Float(FLOAT, "FLOAT", "encodeFloat", "decodeFloat"),
    Double(DOUBLE, "DOUBLE", "encodeDouble", "decodeDouble"),
    Boolean(BOOLEAN, "BOOLEAN", "encodeBoolean", "decodeBoolean"),
}

/**
 * The [TypedEnumKind] of this enum, when it is a set of members all of the declared integer, number or boolean type;
 * `null` for a string enum and for an enum whose members do not all match the declared type (an untyped or mixed-type
 * enum, or one with a `null` member), which keep being rendered as a plain string enum.
 */
internal fun Model.Enum.typedKind(): TypedEnumKind? {
    val kind = when (inner) {
        is Model.Primitive.Int -> TypedEnumKind.Int
        is Model.Primitive.Long -> TypedEnumKind.Long
        is Model.Primitive.Float -> TypedEnumKind.Float
        is Model.Primitive.Double -> TypedEnumKind.Double
        is Model.Primitive.Boolean -> TypedEnumKind.Boolean
        else -> return null
    }
    val matches = values.isNotEmpty() && values.all { value ->
        when (kind) {
            TypedEnumKind.Int -> value is Model.EnumValue.Int
            TypedEnumKind.Long -> value is Model.EnumValue.Long
            TypedEnumKind.Float -> value is Model.EnumValue.Float
            TypedEnumKind.Double -> value is Model.EnumValue.Double
            TypedEnumKind.Boolean -> value is Model.EnumValue.Boolean
        }
    }
    return kind.takeIf { matches }
}

private fun Model.EnumValue.typedLiteral(): CodeBlock = when (this) {
    is Model.EnumValue.Int -> CodeBlock.of("%L", value)
    is Model.EnumValue.Long -> CodeBlock.of("%LL", value)
    is Model.EnumValue.Float -> CodeBlock.of("%Lf", value)
    is Model.EnumValue.Double -> CodeBlock.of("%L", value)
    is Model.EnumValue.Boolean -> CodeBlock.of("%L", value)
    is Model.EnumValue.String, is Model.EnumValue.Null -> error("Not a typed enum value: $this")
}

/**
 * The `String` form of the enum value [expr] in a path, query, header or form value: the canonical text of a typed
 * enum's number or boolean (`300`, `0.5`, `true`), the wire value of a string enum.
 */
internal fun Model.Enum.wireTextExpr(expr: String): String = when {
    typedKind() != null -> "$expr.value.toString()"
    needsValueProperty() -> "$expr.value"
    else -> "$expr.toString()"
}

private val PrimitiveSerialDescriptorMember = MemberName("kotlinx.serialization.descriptors", "PrimitiveSerialDescriptor")
private val PrimitiveKindType = ClassName("kotlinx.serialization.descriptors", "PrimitiveKind")
private val SerializationExceptionType =ClassName("kotlinx.serialization", "SerializationException")

/**
 * The nested `Serializer` of a typed enum: it writes and reads the member's `value` as a JSON number or boolean and
 * is strict, a value of another JSON type or one that is not a member is a `SerializationException`.
 */
private fun typedEnumSerializer(enumName: String, kind: TypedEnumKind): TypeSpec {
    val enumClassName = ClassName("", enumName)
    val dollar = "$"
    val deserialize = CodeBlock.builder()
        // Decodes exactly like the primitive it wraps: how lenient that is, is up to the `Json` instance.
        .addStatement("val value = decoder.%L()", kind.decode)
        .addStatement("val member = %L.entries.firstOrNull·{ it.value·==·value }", enumName)
        .addStatement("return member ?: throw %T(%P)", SerializationExceptionType, "$enumName has no member with value ${dollar}value")
        .build()

    return TypeSpec.objectBuilder("Serializer")
        .addModifiers(KModifier.INTERNAL)
        .addSuperinterface(UnionKSerializerType.parameterizedBy(enumClassName))
        .addProperty(
            PropertySpec.builder("descriptor", UnionSerialDescriptorType)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%M(%S, %T.%L)", PrimitiveSerialDescriptorMember, enumName, PrimitiveKindType, kind.primitiveKind)
                .build()
        )
        .addFunction(
            FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", UnionEncoderType)
                .addParameter("value", enumClassName)
                .addStatement("encoder.%L(value.value)", kind.encode)
                .build()
        )
        .addFunction(
            FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .returns(enumClassName)
                .addParameter("decoder", UnionDecoderType)
                .addCode(deserialize)
                .build()
        )
        .build()
}

fun Model.Enum.toTypeSpec(
    config: RenderConfig,
    parentInterface: ClassName? = null,
    serialName: String? = null,
    nameOverride: String? = null,
    overrideValueProperty: Boolean = false,
): TypeSpec {
    val className = context.toClassName(config)
    val typed = typedKind().takeIf { !overrideValueProperty }
    val enumName = nameOverride ?: className.simpleName
    val withValue = typed != null || needsValueProperty() || overrideValueProperty
    val valueType = typed?.kotlinType ?: STRING
    return TypeSpec.enumBuilder(enumName)
        .addAnnotation(
            if (typed != null) {
                AnnotationSpec.builder(Serializable::class).addMember("with = %L.Serializer::class", enumName).build()
            } else {
                AnnotationSpec.builder(Serializable::class).build()
            }
        )
        .apply {
            if (withValue) {
                primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder("value", valueType).build())
                        .build()
                )
                addProperty(
                    PropertySpec.builder("value", valueType)
                        .apply {
                            if (overrideValueProperty) {
                                addModifiers(KModifier.OVERRIDE)
                            }
                        }
                        .initializer("value")
                        .build()
                )
            }

            serialName?.let { value ->
                addAnnotation(
                    AnnotationSpec.builder(SerialName::class)
                        .addMember("%S", value)
                        .build()
                )
            }
            parentInterface?.let(::addSuperinterface)

            val entryNames = entryNames()
            values.distinctBy { it.wireValue() }.forEach { enumValue ->
                val rawValue = enumValue.wireValue()
                val entryName = entryNames.nameOf(rawValue)
                val entry = TypeSpec.anonymousClassBuilder()

                // A typed enum's `Serializer` writes the entry's `value`, so it needs no `@SerialName`.
                if (typed == null && rawValue != entryName.unescapeBackticks()) {
                    entry.addAnnotation(AnnotationSpec.builder(SerialName::class).addMember("%S", rawValue).build())
                }
                // When the enum has a value constructor, every entry must supply the wire value,
                // even those whose rawValue matches their Kotlin entry name.
                if (typed != null) {
                    entry.addSuperclassConstructorParameter(enumValue.typedLiteral())
                } else if (withValue) {
                    entry.addSuperclassConstructorParameter("%S", rawValue)
                }

                if (KmpTarget.JS in config.targets && entryName.needsJsName()) {
                    entry.addAnnotation(
                        AnnotationSpec.builder(ClassName("kotlin.js", "JsName"))
                            .addMember("%S", entryName.toJsNameValue())
                            .build()
                    )
                }

                addEnumConstant(entryName, entry.build())
            }

            if (typed != null) addType(typedEnumSerializer(enumName, typed))
        }
        .build()
}

fun Model.Enum.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .build()
}
