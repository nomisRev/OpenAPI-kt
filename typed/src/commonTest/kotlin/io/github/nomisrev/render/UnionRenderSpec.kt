package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.render.render
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.render.Import
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.reference
import kotlin.test.assertTrue

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

    fun objectCase(
        name: String,
        vararg props: Pair<String, Model>,
        additionalProperties: Boolean = false,
    ) = Model.Object(
        context = NamingContext(
            NamingContext.Reference("Union", SchemaContext.Null),
            listOf(NamingContext.UnionCase(name))
        ),
        description = null,
        title = null,
        properties = props.associate { (k, v) -> k to Model.Object.Property(v, true) },
        additionalProperties = additionalProperties,
        isNullable = false
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

    fun assertOrdered(text: String, first: String, second: String) {
        val firstIndex = text.indexOf(first)
        val secondIndex = text.indexOf(second)
        assertTrue(firstIndex >= 0, "Could not find '$first' in:\n$text")
        assertTrue(secondIndex >= 0, "Could not find '$second' in:\n$text")
        assertTrue(firstIndex < secondIndex, "Expected '$first' before '$second' in:\n$text")
    }

    verifyKotlinFiles(
        name = "union renders all primitive cases",
        resourceDirectory = "union/all-primitives"
    ) {
        listOf(
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
            )
        ).generate()
    }

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
            "\$type",
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
            "\$type",
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

    test("Nullable oneOf [string, null] property is flattened (no explicit null union case)") {
        val schema = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "value" to ReferenceOr.value(
                    Schema(
                        oneOf = listOf(
                            ReferenceOr.value(Schema.string),
                            ReferenceOr.value(Schema.NULL)
                        )
                    )
                )
            ),
            required = listOf("value")
        )
        val model = registry(api.reference("Container", schema)) {
            ReferenceOr.schema("Container")
                .toModel(NamingContext.Reference("Container", SchemaContext.Null), SchemaContext.Write)
        }

        assertTrue(model is Model.Object, "Expected object model but found: $model")
        val value = model.properties["value"]?.model
        assertTrue(value is Model.Primitive.String, "Expected flattened primitive but found: $value")
        assertTrue(value.isNullable, "Expected flattened primitive to be nullable")
    }

    test("Union with empty object case renders data object Empty") {
        val model = Model.Union(
            context = union,
            cases = listOf(
                Model.Union.Case(objectCase("Empty"), null),
                Model.Union.Case(
                    objectCase("WithProp", "prop" to Model.Primitive.String(null, null, null, false, null)),
                    null
                ),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false
        )

        val (rendered, _) = renderer { model.render() }
        assertTrue(rendered.contains("data object Empty : Union"))
        assertOrdered(
            rendered,
            "WithProp::class to { decodeFromJsonElement(WithProp.serializer(), it) }",
            "Empty::class to { decodeFromJsonElement(Empty.serializer(), it) }"
        )
    }

    test("Union additionalProperties object case is ordered last among object-like cases") {
        val model = Model.Union(
            context = union,
            cases = listOf(
                Model.Union.Case(
                    objectCase("Strict", "name" to Model.Primitive.String(null, null, null, false, null)),
                    null
                ),
                Model.Union.Case(
                    objectCase(
                        "WithAdditional",
                        "name" to Model.Primitive.String(null, null, null, false, null),
                        additionalProperties = true,
                    ),
                    null
                ),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false
        )
        val (rendered, _) = renderer { model.render() }
        assertOrdered(
            rendered,
            "Strict::class to { decodeFromJsonElement(Strict.serializer(), it) }",
            "WithAdditional::class to { decodeFromJsonElement(WithAdditional.serializer(), it) }"
        )
    }

    test("Union FreeFormJson case is dead last in deserialize attempts") {
        val model = Model.Union(
            context = union,
            cases = listOf(
                Model.Union.Case(
                    objectCase("Id", "id" to Model.Primitive.Int(null, null, null, false, null)),
                    null
                ),
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(Model.FreeFormJson(null, null, false, null), null),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false
        )
        val (rendered, _) = renderer { model.render() }
        assertOrdered(
            rendered,
            "CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) }",
            "CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) }"
        )
        assertOrdered(
            rendered,
            "Id::class to { decodeFromJsonElement(Id.serializer(), it) }",
            "CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) }"
        )
    }

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

    test("Nested union case renders as wrapped value class and nested sealed union") {
        val nested = Model.Union(
            context = union.nest(NamingContext.UnionCase("Inner")),
            cases = listOf(
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false
        )

        val model = Model.Union(
            context = union,
            cases = listOf(
                Model.Union.Case(nested, null),
                Model.Union.Case(Model.Primitive.Boolean(null, null, false, null), null),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false
        )

        val (rendered, _) = renderer { model.render() }
        assertTrue(rendered.contains("value class CaseInner(val value: Inner) : Union"))
        assertTrue(rendered.contains("sealed interface Inner"))
        assertTrue(
            rendered.contains(
                "CaseInner::class to { CaseInner(decodeFromJsonElement(Inner.serializer(), it)) }"
            )
        )
    }

    test("Nested discriminated object case renders as wrapped value class") {
        val authContext = union.nest(NamingContext.UnionCase("Auth"))
        val kindProp = Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true)
        val auth = Model.DiscriminatedObject(
            context = authContext,
            abstractProperties = mapOf("kind" to kindProp),
            subtypes = listOf(
                Model.Object(
                    context = authContext.nest(NamingContext.DiscriminatedObjectCase("password")),
                    description = null,
                    title = null,
                    properties = mapOf(
                        "kind" to kindProp,
                        "password" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true)
                    ),
                    additionalProperties = false,
                    isNullable = false
                ),
                Model.Object(
                    context = authContext.nest(NamingContext.DiscriminatedObjectCase("token")),
                    description = null,
                    title = null,
                    properties = mapOf(
                        "kind" to kindProp,
                        "token" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true)
                    ),
                    additionalProperties = false,
                    isNullable = false
                )
            ),
            description = null,
            title = null,
            discriminator = "kind",
            isNullable = false
        )

        val model = Model.Union(
            context = union,
            cases = listOf(
                Model.Union.Case(auth, null),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false
        )

        val (rendered, _) = renderer { model.render() }
        assertTrue(rendered.contains("value class CaseAuth(val value: Auth) : Union"))
        assertTrue(rendered.contains("@JsonClassDiscriminator(\"kind\")"))
        assertTrue(rendered.contains("sealed interface Auth"))
        assertTrue(
            rendered.contains(
                "CaseAuth::class to { CaseAuth(decodeFromJsonElement(Auth.serializer(), it)) }"
            )
        )
    }

    test("Recursive union through \$ref renders without infinite expansion") {
        val treeSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf("leaf" to ReferenceOr.value(Schema.string))
                    )
                ),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf(
                            "children" to ReferenceOr.value(
                                Schema.list(ReferenceOr.schema("Tree"))
                            )
                        )
                    )
                )
            )
        )

        val model = registry(api.reference("Tree", treeSchema)) {
            ReferenceOr.schema("Tree")
                .toModel(NamingContext.Reference("Tree", SchemaContext.Null), SchemaContext.Write)
        }
        val (rendered, _) = renderer { model.render() }
        assertTrue(rendered.contains("List<Tree>"))
        assertTrue(rendered.contains("value class CaseTree(val value: Tree) : Union").not())
    }
}
