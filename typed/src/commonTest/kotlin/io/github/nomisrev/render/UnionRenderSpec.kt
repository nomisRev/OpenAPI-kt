package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.render.Import
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.SchemaContext

val unionRenderSpec by testSuite {
    val union = NamingContext.reference("Union", SchemaContext.Null)
    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseInt(val value: Int) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseFloat(val value: Float) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseDouble(val value: Double) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseLocalDate(val value: LocalDate) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseLocalDateTime(val value: LocalDateTime) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseByteArray(val value: ByteArray) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseUuid(val value: Uuid) : Union
            |
            |    @Serializable
            |    data object Empty : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CaseString", String.serializer().descriptor)
            |                element("CaseInt", Int.serializer().descriptor)
            |                element("CaseFloat", Float.serializer().descriptor)
            |                element("CaseDouble", Double.serializer().descriptor)
            |                element("CaseLocalDate", LocalDate.serializer().descriptor)
            |                element("CaseLocalDateTime", LocalDateTime.serializer().descriptor)
            |                element("CaseByteArray", ByteArraySerializer().descriptor)
            |                element("CaseUuid", Uuid.serializer().descriptor)
            |                element("CaseUnit", Unit.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return attemptDeserialize(
            |                value,
            |                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
            |                CaseFloat::class to { CaseFloat(decodeFromJsonElement(Float.serializer(), it)) },
            |                CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
            |                CaseUnit::class to { CaseUnit(decodeFromJsonElement(Unit.serializer(), it)) },
            |                CaseUuid::class to { CaseUuid(decodeFromJsonElement(Uuid.serializer(), it)) },
            |                CaseLocalDate::class to { CaseLocalDate(decodeFromJsonElement(LocalDate.serializer(), it)) },
            |                CaseLocalDateTime::class to { CaseLocalDateTime(decodeFromJsonElement(LocalDateTime.serializer(), it)) },
            |                CaseByteArray::class to { CaseByteArray(decodeFromJsonElement(ByteArraySerializer(), it)) },
            |                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            |            is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
            |            is CaseFloat -> encoder.encodeSerializableValue(Float.serializer(), value.value)
            |            is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
            |            is CaseLocalDate -> encoder.encodeSerializableValue(LocalDate.serializer(), value.value)
            |            is CaseLocalDateTime -> encoder.encodeSerializableValue(LocalDateTime.serializer(), value.value)
            |            is CaseByteArray -> encoder.encodeSerializableValue(ByteArraySerializer(), value.value)
            |            is CaseUuid -> encoder.encodeSerializableValue(Uuid.serializer(), value.value)
            |            is CaseUnit -> encoder.encodeSerializableValue(Unit.serializer(), value.value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.Float(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.Double(null, null, null, false, null), null),
                Model.Union.Case(Model.Date(null, false, null), null),
                Model.Union.Case(Model.DateTime(null, false, null), null),
                Model.Union.Case(Model.ByteArray(null, false, null), null),
                Model.Union.Case(Model.Uuid(null, false, null), null),
                Model.Union.Case(Model.Primitive.Unit(null, false, null), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.Uuid,
        TypeName.Date,
        TypeName.DateTime,
        TypeName.ExperimentalSerializationApi,
        TypeName.InternalSerializationApi,
        TypeName.PolymorphicKind,
        Import.serializer,
        Import.ByteArraySerializer,
        TypeName.JsonElement,
        TypeName.JsonDecoder,
    )

    fun employeeCase(ctx: NamingContext.Nested) = Model.Object(
        context = NamingContext(NamingContext.Reference("Union", SchemaContext.Null), listOf(ctx)),
        description = null,
        title = null,
        properties = mapOf(
            "age" to Model.Object.Property(Model.Primitive.Int(null, null, null, false, null), true),
            "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true),
        ),
        additionalProperties = false,
        isNullable = false
    )

    // TODO, similarly to enum name gen try to generate AgeAndName object name instead of Case1
    //  Only fallback to CaseIndex if name exceeds certain length
    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |
            |    @Serializable
            |    data class Case1(val age: Int, val name: String) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CaseString", String.serializer().descriptor)
            |                element("CaseCase1", Case1.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return attemptDeserialize(
            |                value,
            |                CaseCase1::class to { decodeFromJsonElement(Case1.serializer(), it) },
            |                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            |            is CaseCase1 -> encoder.encodeSerializableValue(Case1.serializer(), value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(employeeCase(NamingContext.UnionCase("Case1")), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.ExperimentalSerializationApi,
        TypeName.InternalSerializationApi,
        TypeName.PolymorphicKind,
        Import.serializer,
        TypeName.JsonElement,
        TypeName.JsonDecoder,
    )

    // TODO: the referenced type needs a flattening serializer.
    verify(
        $$$"""
            |@JsonClassDiscriminator($$"$type")
            |@Serializable
            |sealed interface Union {
            |    @SerialName("reference")
            |    @Serializable
            |    @JvmInline
            |    value class Reference(val value: String) : Union
            |
            |    @SerialName("employee")
            |    @Serializable
            |    data class Employee(val age: Int, val name: String) : Union
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), "reference"),
                Model.Union.Case(employeeCase(NamingContext.UnionCase("employee")), "employee"),
            ),
            null,
            null,
            null,
            $$"$type",
            false
        ),
        TypeName.JsonClassDiscriminator,
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.SerialName
    )

    val aOrB = Model.Enum(
        context = union.nest(NamingContext.UnionCase("AscOrDesc")),
        inner = Model.Primitive.String(null, null, null, false, null),
        values = listOf("asc", "desc"),
        default = null,
        description = null,
        title = null,
        isNullable = false
    )

    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |
            |    @Serializable
            |    enum class AscOrDesc : Union {
            |        @SerialName("asc") Asc, @SerialName("desc") Desc;
            |    }
            |
            |    object Serializer : KSerializer<Union> {
            |        override val descriptor: SerialDescriptor = String.serializer().descriptor
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            AscOrDesc.Asc -> encoder.encodeString("asc")
            |            AscOrDesc.Desc -> encoder.encodeString("desc")
            |            is CaseString -> encoder.encodeString(value.value)
            |        }
            |
            |        override fun deserialize(decoder: Decoder): Union =
            |            when(val value = decoder.decodeString()) {
            |                "asc" -> AscOrDesc.Asc
            |                "desc" -> AscOrDesc.Desc
            |                else -> CaseString(value)
            |            }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(aOrB, null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.SerialName,
        Import.serializer,
    )

    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseStrings(val value: List<String>) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseInt(val value: Int) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CaseStrings", ListSerializer(String.serializer()).descriptor)
            |                element("CaseInt", Int.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return attemptDeserialize(
            |                value,
            |                CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
            |                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
            |            is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(
                    Model.Collection(
                        Model.Primitive.String(null, null, null, false, null),
                        null,
                        null,
                        null,
                        false,
                        null
                    ),
                    null
                ),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.ExperimentalSerializationApi,
        TypeName.InternalSerializationApi,
        TypeName.PolymorphicKind,
        Import.ListSerializer,
        Import.serializer,
        TypeName.JsonElement,
        TypeName.JsonDecoder,
    )

    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseStringsList(val value: List<List<String>>) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseInt(val value: Int) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CaseStringsList", ListSerializer(ListSerializer(String.serializer())).descriptor)
            |                element("CaseInt", Int.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return attemptDeserialize(
            |                value,
            |                CaseStringsList::class to { CaseStringsList(decodeFromJsonElement(ListSerializer(ListSerializer(String.serializer())), it)) },
            |                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseStringsList -> encoder.encodeSerializableValue(ListSerializer(ListSerializer(String.serializer())), value.value)
            |            is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(
                    Model.Collection(
                        Model.Collection(
                            Model.Primitive.String(null, null, null, false, null),
                            null,
                            null,
                            null,
                            false,
                            null
                        ),
                        null,
                        null,
                        null,
                        false,
                        null
                    ),
                    null
                ),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.ExperimentalSerializationApi,
        TypeName.InternalSerializationApi,
        TypeName.PolymorphicKind,
        Import.ListSerializer,
        Import.serializer,
        TypeName.JsonElement,
        TypeName.JsonDecoder,
    )
}
