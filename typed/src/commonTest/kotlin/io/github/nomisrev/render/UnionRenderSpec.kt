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
            |    value class CaseDate(val value: LocalDate) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseDateTime(val value: LocalDateTime) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseBinary(val value: ByteArray) : Union
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
            |                element("CaseDate", LocalDate.serializer().descriptor)
            |                element("CaseDateTime", LocalDateTime.serializer().descriptor)
            |                element("CaseBinary", ByteArraySerializer().descriptor)
            |                element("CaseUuid", Uuid.serializer().descriptor)
            |                element("CaseUnit", Unit.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return json.attemptDeserialize(
            |                value,
            |                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
            |                CaseFloat::class to { CaseFloat(decodeFromJsonElement(Float.serializer(), it)) },
            |                CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
            |                CaseUnit::class to { CaseUnit(decodeFromJsonElement(Unit.serializer(), it)) },
            |                CaseUuid::class to { CaseUuid(decodeFromJsonElement(Uuid.serializer(), it)) },
            |                CaseDate::class to { CaseDate(decodeFromJsonElement(LocalDate.serializer(), it)) },
            |                CaseDateTime::class to { CaseDateTime(decodeFromJsonElement(LocalDateTime.serializer(), it)) },
            |                CaseBinary::class to { CaseBinary(decodeFromJsonElement(ByteArraySerializer(), it)) },
            |                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            |            is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
            |            is CaseFloat -> encoder.encodeSerializableValue(Float.serializer(), value.value)
            |            is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
            |            is CaseDate -> encoder.encodeSerializableValue(LocalDate.serializer(), value.value)
            |            is CaseDateTime -> encoder.encodeSerializableValue(LocalDateTime.serializer(), value.value)
            |            is CaseBinary -> encoder.encodeSerializableValue(ByteArraySerializer(), value.value)
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
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        Import.buildSerialDescriptor
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

    // Union case names are derived from property names joined with "And"
    // Falls back to CaseIndex if the generated name exceeds 90 characters
    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |
            |    @Serializable
            |    data class AgeAndName(val age: Int, val name: String) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CaseString", String.serializer().descriptor)
            |                element("AgeAndName", AgeAndName.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return json.attemptDeserialize(
            |                value,
            |                AgeAndName::class to { decodeFromJsonElement(AgeAndName.serializer(), it) },
            |                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            |            is AgeAndName -> encoder.encodeSerializableValue(AgeAndName.serializer(), value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(employeeCase(NamingContext.UnionCase("AgeAndName")), null),
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
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        Import.buildSerialDescriptor
    )

    // TODO: the referenced type needs a flattening serializer.
    verify(
        $$$"""
            |@OptIn(ExperimentalSerializationApi::class)
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
        TypeName.ExperimentalSerializationApi,
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
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder
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
            |            return json.attemptDeserialize(
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
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        Import.buildSerialDescriptor
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
            |            return json.attemptDeserialize(
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
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        Import.buildSerialDescriptor
    )

    // Union with $ref cases generates value class wrappers that flatten serialization
    // e.g., oneOf: [$ref: Person, $ref: Company] generates CasePerson(val value: Person)
    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CasePerson(val value: Person) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseCompany(val value: Company) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CasePerson", Person.serializer().descriptor)
            |                element("CaseCompany", Company.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return json.attemptDeserialize(
            |                value,
            |                CasePerson::class to { CasePerson(decodeFromJsonElement(Person.serializer(), it)) },
            |                CaseCompany::class to { CaseCompany(decodeFromJsonElement(Company.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CasePerson -> encoder.encodeSerializableValue(Person.serializer(), value.value)
            |            is CaseCompany -> encoder.encodeSerializableValue(Company.serializer(), value.value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(
                    Model.Reference(
                        NamingContext.reference("Person", SchemaContext.Null),
                        null,
                        false,
                        null
                    ),
                    null
                ),
                Model.Union.Case(
                    Model.Reference(
                        NamingContext.reference("Company", SchemaContext.Null),
                        null,
                        false,
                        null
                    ),
                    null
                ),
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
        TypeName.Class("io.github.nomisrev.model", "Person"),
        TypeName.Class("io.github.nomisrev.model", "Company"),
        TypeName.JsonElement,
        TypeName.JsonDecoder,
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        Import.buildSerialDescriptor
    )
}
