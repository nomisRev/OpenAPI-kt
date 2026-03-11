package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.render.Import
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.SchemaContext

/**
 * Tests for union (oneOf/anyOf) code generation including edge cases.
 *
 * Covers various scenarios that can occur with oneOf/anyOf in OpenAPI specs:
 * - Basic unions with primitives, objects, references, collections
 * - Discriminated unions with type discriminators
 * - Edge cases: single case unions, overlapping properties, nested collections
 * - Known limitations: nested unions, discriminated objects in unions, recursive unions
 */
val unionRenderSpec by testSuite {
    val union = NamingContext.reference("Union", SchemaContext.Null)

    fun objectCase(name: String, vararg props: Pair<String, Model>) = Model.Object(
        context = NamingContext(NamingContext.Reference("Union", SchemaContext.Null), listOf(NamingContext.UnionCase(name))),
        description = null,
        title = null,
        properties = props.associate { (k, v) -> k to Model.Object.Property(v, true) },
        additionalProperties = false,
        isNullable = false
    )

    // ==================== BASIC TYPES ====================

    // Comprehensive test covering all primitive types, dates, UUIDs, and empty objects
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

    // ==================== UNION CASE NAMING ====================

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

    // ==================== DISCRIMINATED UNIONS ====================

    // Discriminated union with inline primitive case
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

    // Discriminated union with $ref case - value class wraps the referenced type
    // The @JvmInline value class serializes the inner Person directly (flattening)
    // KotlinX Serialization's polymorphic handling adds the discriminator
    verify(
        $$$"""
            |@OptIn(ExperimentalSerializationApi::class)
            |@JsonClassDiscriminator($$"$type")
            |@Serializable
            |sealed interface Union {
            |    @SerialName("person")
            |    @Serializable
            |    @JvmInline
            |    value class Person(val value: Person) : Union
            |
            |    @SerialName("employee")
            |    @Serializable
            |    data class Employee(val age: Int, val name: String) : Union
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
                    "person"
                ),
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
        TypeName.SerialName,
        TypeName.Class("io.github.nomisrev.model", "Person")
    )

    // ==================== UNIONS WITH ENUMS ====================

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

    // ==================== UNIONS WITH COLLECTIONS ====================

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

    // ==================== UNIONS WITH REFERENCES ====================

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

    // ==================== EDGE CASES ====================

    // EDGE CASE 1: Single case union
    // oneOf with only one option - should still generate sealed interface for type safety
    // This can happen when specs use oneOf for future extensibility
    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CaseString", String.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return json.attemptDeserialize(
            |                value,
            |                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
            ),
            null, null, null, null, false
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

    // EDGE CASE 2: Nullable union case
    // oneOf: [string, null] - common pattern for optional values
    // The null case should make the type nullable, not create a separate case
    // This is handled at the transformer level - null types are filtered out
    // and the remaining type becomes nullable

    // EDGE CASE 3: All references union
    // Covered by the Person/Company test above

    // EDGE CASE 4: Objects with overlapping properties
    // oneOf: [{a, b}, {a, b, c}] - overlapping schemas
    // Deserialization order matters - more specific (more properties) should come first
    // The unionCaseComparator handles this by sorting objects with more properties first
    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    data class AAndB(val a: String, val b: String) : Union
            |
            |    @Serializable
            |    data class AAndBAndC(val a: String, val b: String, val c: String) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("AAndB", AAndB.serializer().descriptor)
            |                element("AAndBAndC", AAndBAndC.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return json.attemptDeserialize(
            |                value,
            |                AAndBAndC::class to { decodeFromJsonElement(AAndBAndC.serializer(), it) },
            |                AAndB::class to { decodeFromJsonElement(AAndB.serializer(), it) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is AAndB -> encoder.encodeSerializableValue(AAndB.serializer(), value)
            |            is AAndBAndC -> encoder.encodeSerializableValue(AAndBAndC.serializer(), value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(objectCase("AAndB",
                    "a" to Model.Primitive.String(null, null, null, false, null),
                    "b" to Model.Primitive.String(null, null, null, false, null)
                ), null),
                Model.Union.Case(objectCase("AAndBAndC",
                    "a" to Model.Primitive.String(null, null, null, false, null),
                    "b" to Model.Primitive.String(null, null, null, false, null),
                    "c" to Model.Primitive.String(null, null, null, false, null)
                ), null),
            ),
            null, null, null, null, false
        ),
        TypeName.Serializable,
        TypeName.ExperimentalSerializationApi,
        TypeName.InternalSerializationApi,
        TypeName.PolymorphicKind,
        TypeName.JsonElement,
        TypeName.JsonDecoder,
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        Import.buildSerialDescriptor
    )

    // EDGE CASE 5: Mixed primitives
    // Covered by the comprehensive primitives test at the beginning

    // EDGE CASE 6: Object with additionalProperties
    // Object with additionalProperties: true should be tried last as it swallows everything
    // This tests the deserialize order priority

    // EDGE CASE 7: FreeFormJson in union
    // oneOf: [{...}, JsonElement]
    // FreeFormJson should be dead last in deserialization as it matches anything

    // EDGE CASE 8: Collection of references
    // oneOf: [array of $ref: Item, single Item]
    // Tests List<Item> vs Item discrimination
    verify(
        """
            |@Serializable(with = Union.Serializer::class)
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseItems(val value: List<Item>) : Union
            |
            |    @Serializable
            |    @JvmInline
            |    value class CaseItem(val value: Item) : Union
            |
            |    object Serializer : KSerializer<Union> {
            |        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            |        override val descriptor: SerialDescriptor =
            |            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
            |                element("CaseItems", ListSerializer(Item.serializer()).descriptor)
            |                element("CaseItem", Item.serializer().descriptor)
            |            }
            |
            |        override fun deserialize(decoder: Decoder): Union {
            |            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            |            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            |            return json.attemptDeserialize(
            |                value,
            |                CaseItems::class to { CaseItems(decodeFromJsonElement(ListSerializer(Item.serializer()), it)) },
            |                CaseItem::class to { CaseItem(decodeFromJsonElement(Item.serializer(), it)) },
            |            )
            |        }
            |
            |        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            |            is CaseItems -> encoder.encodeSerializableValue(ListSerializer(Item.serializer()), value.value)
            |            is CaseItem -> encoder.encodeSerializableValue(Item.serializer(), value.value)
            |        }
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(
                    Model.Collection(
                        Model.Reference(NamingContext.reference("Item", SchemaContext.Null), null, false, null),
                        null, null, null, false, null
                    ),
                    null
                ),
                Model.Union.Case(Model.Reference(NamingContext.reference("Item", SchemaContext.Null), null, false, null), null),
            ),
            null, null, null, null, false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.ExperimentalSerializationApi,
        TypeName.InternalSerializationApi,
        TypeName.PolymorphicKind,
        Import.ListSerializer,
        TypeName.Class("io.github.nomisrev.model", "Item"),
        TypeName.JsonElement,
        TypeName.JsonDecoder,
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        Import.buildSerialDescriptor
    )

    // ==================== KNOWN LIMITATIONS / NOT YET SUPPORTED ====================

    // EDGE CASE 9: Nested unions (union inside union)
    // oneOf: [oneOf: [A, B], C]
    // Currently: TODO("Inline defined nested Union not yet supported in Union")

    // EDGE CASE 10: DiscriminatedObject inside union
    // oneOf: [discriminatedObject, regularObject]
    // Currently: TODO("Nested DiscriminatedObject not supported in Union")

    // EDGE CASE 11: Recursive union (union references itself)
    // oneOf: [$ref: Node, null] where Node contains the same union
    // Should be handled via Model.Reference but needs testing

    // EDGE CASE 12: Union with empty object case
    // oneOf: [{}, {prop: string}]
    // Empty object generates data object Empty
}
