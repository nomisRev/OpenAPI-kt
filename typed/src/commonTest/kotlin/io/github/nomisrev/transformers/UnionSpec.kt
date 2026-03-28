package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.reference
import io.github.nomisrev.zip
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.AnyOf
import io.github.nomisrev.openapi.Model.OneOf
import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.product
import io.github.nomisrev.verifyAll
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

val unionSpec by testSuite {
    fun case(model: Model, vararg discriminatorValues: String): Union.Case =
        Union.Case(model, discriminatorValues.toSet())

    fun moderationInputUnionSchema(): Schema =
        Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.string),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Array,
                        items = ReferenceOr.value(Schema.string)
                    )
                ),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Array,
                        items = ReferenceOr.value(
                            Schema(
                                oneOf = listOf(
                                    ReferenceOr.value(
                                        Schema(
                                            type = Schema.Type.Basic.Object,
                                            properties = mapOf(
                                                "type" to ReferenceOr.value(
                                                    Schema(
                                                        type = Schema.Type.Basic.String,
                                                        enum = listOf("image_url")
                                                    )
                                                ),
                                                "image_url" to ReferenceOr.value(
                                                    Schema(
                                                        type = Schema.Type.Basic.Object,
                                                        properties = mapOf(
                                                            "url" to ReferenceOr.value(Schema.string)
                                                        ),
                                                        required = listOf("url")
                                                    )
                                                )
                                            ),
                                            required = listOf("type", "image_url")
                                        )
                                    ),
                                    ReferenceOr.value(
                                        Schema(
                                            type = Schema.Type.Basic.Object,
                                            properties = mapOf(
                                                "type" to ReferenceOr.value(
                                                    Schema(
                                                        type = Schema.Type.Basic.String,
                                                        enum = listOf("text")
                                                    )
                                                ),
                                                "text" to ReferenceOr.value(Schema.string)
                                            ),
                                            required = listOf("type", "text")
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

    val unions =
        Model.Primitive.String.all().zip(
            Model.Primitive.Long.all(),
            Model.Primitive.Double.all(),
            Model.Primitive.Boolean.all()
        ) { s, l, d, b ->
            listOf(
                ReferenceOr.value(s.actual),
                ReferenceOr.value(l.actual),
                ReferenceOr.value(d.actual),
                ReferenceOr.value(b.actual),
            ) expect listOf(
                case(s.expected),
                case(l.expected),
                case(d.expected),
                case(b.expected),
            )
        }

    verifyAll(
        "Union primitives",
        unions.product(description, listOf(true, false, null)) { oneOf, description, isNullable ->
            val actual = Schema(
                oneOf = oneOf.actual,
                description = description.actual,
                nullable = isNullable
            )
            val expected = Model.OneOf(
                NamingContext.path("test"),
                oneOf.expected,
                null,
                description.expected,
                null,
                UnionDispatch.Structural,
                isNullable ?: false
            )

            actual expect expected
        }
    )

    // Test for OpenEnum pattern: anyOf[{ type: String }, { type: String, enum: [a, b] }]
    // The enum case should inherit the outer name: CaseString & CaseModel
    test("OpenEnum pattern - enum case inherits outer name") {
        val openEnumSchema = Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema.string),
                ReferenceOr.value(Schema.string.copy(enum = listOf("a", "b")))
            )
        )
        val testApi = api.reference("Model", openEnumSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Model")
                .toModel(NamingContext.reference("Model", SchemaContext.Null), SchemaContext.Write)
            val expected = AnyOf(
                NamingContext.reference("Model", SchemaContext.Null),
                listOf(
                    case(Model.Primitive.String(null, null, null, false, null)),
                    case(
                        Model.Enum(
                            NamingContext.reference("Model", SchemaContext.Null)
                                .nest(NamingContext.UnionCase("AOrB")),
                            Model.Primitive.String(null, null, null, false, null),
                            listOf(Model.EnumValue.String("a"), Model.EnumValue.String("b")), null, null, null, false
                        )
                    )
                ),
                null,
                null,
                null,
                UnionDispatch.Structural,
                false
            )
            assertEquals(expected, result)
        }
    }

    // Test for reference pattern: oneOf[{ $ref:EventA }, { $ref:EventB }, { type: object }]
    // The non-reference case should inherit the outer name: EventA, EventB, CaseEvent
    test("Reference pattern - non-reference case inherits outer name") {
        val eventASchema = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("value" to ReferenceOr.value(Schema.string))
        )
        val eventBSchema = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("value" to ReferenceOr.value(Schema.string))
        )
        val eventUnionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("EventA"),
                ReferenceOr.schema("EventB"),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf("value" to ReferenceOr.value(Schema.string))
                    )
                )
            )
        )
        val testApi = api
            .reference("EventA", eventASchema)
            .reference("EventB", eventBSchema)
            .reference("Event", eventUnionSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Event")
                .toModel(NamingContext.reference("Event", SchemaContext.Null), SchemaContext.Write)

            val expected = OneOf(
                NamingContext.reference("Event", SchemaContext.Null),
                listOf(
                    case(
                        Model.Reference(
                            NamingContext.reference("EventA", SchemaContext.Null),
                            null,
                            false,
                            null
                        )
                    ),
                    case(
                        Model.Reference(
                            NamingContext.reference("EventB", SchemaContext.Null),
                            null,
                            false,
                            null
                        )
                    ),
                    case(
                        Model.Object(
                            NamingContext.reference("Event", SchemaContext.Null)
                                .nest(NamingContext.UnionCase("CaseElse")), null, null, mapOf(
                                "value" to Model.Object.Property(
                                    Model.Primitive.String(null, null, null, false, null),
                                    false
                                )
                            ), false, false
                        )
                    )
                ),
                null,
                null, null, UnionDispatch.Structural, false
            )

            assertEquals(expected, result)
        }
    }

    test("Discriminated union infers implicit mapping from referenced schema names") {
        val directory = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val file = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val contentSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("content-directory"),
                ReferenceOr.schema("content-file")
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )
        val testApi = api
            .reference("content-directory", directory)
            .reference("content-file", file)
            .reference("content", contentSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("content")
                .toModel(NamingContext.reference("content", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals(UnionDispatch.NativeDiscriminator("type"), result.dispatch)
            assertEquals(
                listOf(setOf("content-directory"), setOf("content-file")),
                result.cases.map { it.discriminatorValues }
            )
            assertTrue(result.cases.all { it.model is Model.Primitive.Unit })
        }
    }

    test("Discriminated union resolves explicit mapping keys from mapping values") {
        val directory = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val file = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val contentSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("content-directory"),
                ReferenceOr.schema("content-file")
            ),
            discriminator = Schema.Discriminator(
                propertyName = "type",
                mapping = mapOf(
                    "dir" to "#/components/schemas/content-directory",
                    "file" to "#/components/schemas/content-file"
                )
            )
        )
        val testApi = api
            .reference("content-directory", directory)
            .reference("content-file", file)
            .reference("content", contentSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("content")
                .toModel(NamingContext.reference("content", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals(UnionDispatch.NativeDiscriminator("type"), result.dispatch)
            assertEquals(listOf(setOf("dir"), setOf("file")), result.cases.map { it.discriminatorValues })
            assertTrue(result.cases.all { it.model is Model.Primitive.Unit })
        }
    }

    test("Discriminated anyOf inlines referenced cases, strips discriminator, and hoists single object payloads") {
        val speechAudioDeltaEvent = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("speech.audio.delta")
                    )
                ),
                "audio" to ReferenceOr.value(Schema.string)
            ),
            required = listOf("type", "audio")
        )
        val speechAudioDoneEvent = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("speech.audio.done")
                    )
                ),
                "usage" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf(
                            "input_tokens" to ReferenceOr.value(Schema.integer),
                            "output_tokens" to ReferenceOr.value(Schema.integer),
                            "total_tokens" to ReferenceOr.value(Schema.integer),
                        ),
                        required = listOf("input_tokens", "output_tokens", "total_tokens")
                    )
                )
            ),
            required = listOf("type", "usage")
        )
        val streamEvent = Schema(
            anyOf = listOf(
                ReferenceOr.schema("SpeechAudioDeltaEvent"),
                ReferenceOr.schema("SpeechAudioDoneEvent")
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )
        val testApi = api
            .reference("SpeechAudioDeltaEvent", speechAudioDeltaEvent)
            .reference("SpeechAudioDoneEvent", speechAudioDoneEvent)
            .reference("CreateSpeechResponseStreamEvent", streamEvent)

        registry(testApi) {
            val result = ReferenceOr.schema("CreateSpeechResponseStreamEvent")
                .toModel(
                    NamingContext.reference("CreateSpeechResponseStreamEvent", SchemaContext.Null),
                    SchemaContext.Write
                ) as AnyOf

            assertEquals(UnionDispatch.NativeDiscriminator("type"), result.dispatch)
            assertEquals(
                listOf(setOf("speech.audio.delta"), setOf("speech.audio.done")),
                result.cases.map { it.discriminatorValues }
            )

            val delta = assertIs<Model.Object>(result.cases[0].model)
            assertEquals(setOf("audio"), delta.properties.keys)
            assertEquals(
                NamingContext.reference("CreateSpeechResponseStreamEvent", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("speech.audio.delta")),
                delta.context
            )

            val done = assertIs<Model.Object>(result.cases[1].model)
            assertEquals(
                setOf("input_tokens", "output_tokens", "total_tokens"),
                done.properties.keys
            )
            assertEquals(
                NamingContext.reference("CreateSpeechResponseStreamEvent", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("speech.audio.done")),
                done.context
            )
            assertTrue(done.properties.values.all { it.model is Model.Primitive.Long })
        }
    }

    test("Discriminated anyOf resolves inherited allOf discriminators and strips inherited tags") {
        val messageItem = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("message")
                    )
                ),
                "text" to ReferenceOr.value(Schema.string)
            ),
            required = listOf("type", "text")
        )
        val functionToolCall = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("function_call")
                    )
                ),
                "call_id" to ReferenceOr.value(Schema.string)
            ),
            required = listOf("type", "call_id")
        )
        val functionToolCallResource = Schema(
            allOf = listOf(
                ReferenceOr.schema("FunctionToolCall"),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf(
                            "id" to ReferenceOr.value(Schema.string)
                        ),
                        required = listOf("id")
                    )
                )
            )
        )
        val conversationItem = Schema(
            anyOf = listOf(
                ReferenceOr.schema("MessageItem"),
                ReferenceOr.schema("FunctionToolCallResource")
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )
        val testApi = api
            .reference("MessageItem", messageItem)
            .reference("FunctionToolCall", functionToolCall)
            .reference("FunctionToolCallResource", functionToolCallResource)
            .reference("ConversationItem", conversationItem)

        registry(testApi) {
            val result = ReferenceOr.schema("ConversationItem")
                .toModel(
                    NamingContext.reference("ConversationItem", SchemaContext.Null),
                    SchemaContext.Write
                ) as AnyOf

            assertEquals(UnionDispatch.NativeDiscriminator("type"), result.dispatch)
            assertEquals(listOf(setOf("message"), setOf("function_call")), result.cases.map { it.discriminatorValues })

            val message = assertIs<Model.Object>(result.cases[0].model)
            assertEquals(setOf("text"), message.properties.keys)
            assertTrue(message.properties.getValue("text").isRequired)

            val functionCall = assertIs<Model.Object>(result.cases[1].model)
            assertEquals(setOf("call_id", "id"), functionCall.properties.keys)
            assertTrue(functionCall.properties.getValue("call_id").isRequired)
            assertTrue(functionCall.properties.getValue("id").isRequired)
            assertEquals(
                NamingContext.reference("ConversationItem", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("function_call")),
                functionCall.context
            )
        }
    }

    test("Ref-only union infers tag-only discriminator and inlines referenced cases") {
        val textPart = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("text")
                    )
                ),
                "text" to ReferenceOr.value(Schema.string)
            ),
            required = listOf("type", "text")
        )
        val imagePart = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("image_url")
                    )
                ),
                "image_url" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf(
                            "url" to ReferenceOr.value(Schema.string)
                        ),
                        required = listOf("url")
                    )
                )
            ),
            required = listOf("type", "image_url")
        )
        val contentPart = Schema(
            oneOf = listOf(
                ReferenceOr.schema("ChatCompletionRequestMessageContentPartText"),
                ReferenceOr.schema("ChatCompletionRequestMessageContentPartImage")
            )
        )
        val testApi = api
            .reference("ChatCompletionRequestMessageContentPartText", textPart)
            .reference("ChatCompletionRequestMessageContentPartImage", imagePart)
            .reference("ChatCompletionRequestUserMessageContentPart", contentPart)

        registry(testApi) {
            val result = ReferenceOr.schema("ChatCompletionRequestUserMessageContentPart")
                .toModel(
                    NamingContext.reference("ChatCompletionRequestUserMessageContentPart", SchemaContext.Null),
                    SchemaContext.Write
                ) as OneOf

            assertEquals(UnionDispatch.NativeDiscriminator("type"), result.dispatch)
            assertEquals(listOf(setOf("text"), setOf("image_url")), result.cases.map { it.discriminatorValues })

            val text = assertIs<Model.Object>(result.cases[0].model)
            assertEquals(setOf("text"), text.properties.keys)
            assertEquals(
                NamingContext.reference("ChatCompletionRequestUserMessageContentPart", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("text")),
                text.context
            )

            val image = assertIs<Model.Object>(result.cases[1].model)
            assertEquals(setOf("url"), image.properties.keys)
            assertEquals(
                NamingContext.reference("ChatCompletionRequestUserMessageContentPart", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("image_url")),
                image.context
            )
        }
    }

    test("Recursive unions with multi-valued discriminator fields use tagged custom dispatch") {
        val comparisonFilter = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("eq", "ne")
                    )
                ),
                "key" to ReferenceOr.value(Schema.string),
                "value" to ReferenceOr.value(Schema.string),
            ),
            required = listOf("type", "key", "value")
        )
        val compoundFilter = Schema(
            recursiveAnchor = true,
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("and", "or")
                    )
                ),
                "filters" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Array,
                        items = ReferenceOr.value(
                            Schema(
                                oneOf = listOf(
                                    ReferenceOr.schema("ComparisonFilter"),
                                    ReferenceOr.Reference("#")
                                ),
                                discriminator = Schema.Discriminator(propertyName = "type")
                            )
                        )
                    )
                )
            ),
            required = listOf("type", "filters")
        )
        val testApi = api
            .reference("ComparisonFilter", comparisonFilter)
            .reference("CompoundFilter", compoundFilter)

        registry(testApi) {
            val result = ReferenceOr.schema("CompoundFilter")
                .toModel(
                    NamingContext.reference("CompoundFilter", SchemaContext.Null),
                    SchemaContext.Write
                )
            val compound = assertIs<Model.Object>(result)
            val filters = assertIs<Model.Collection>(compound.properties.getValue("filters").model)
            val union = assertIs<Model.OneOf>(filters.inner)

            assertEquals(
                NamingContext.reference("CompoundFilter", SchemaContext.Null)
                    .nest(NamingContext.ObjectProperty("filters")),
                union.context
            )
            assertEquals(UnionDispatch.TaggedCustom("type"), union.dispatch)
            assertEquals(
                listOf(setOf("eq", "ne"), setOf("and", "or")),
                union.cases.map { it.discriminatorValues }
            )

            val comparison = assertIs<Model.Object>(union.cases[0].model)
            assertEquals(
                NamingContext.reference("CompoundFilter", SchemaContext.Null)
                    .nest(NamingContext.ObjectProperty("filters"))
                    .nest(NamingContext.UnionCase("ComparisonFilter")),
                comparison.context
            )
            assertEquals(setOf("type", "key", "value"), comparison.properties.keys)
            assertTrue(comparison.properties.getValue("type").isRequired)

            val recursive = assertIs<Model.Reference>(union.cases[1].model)
            assertEquals(NamingContext.reference("CompoundFilter", SchemaContext.Null), recursive.context)
        }
    }

    test("Union preserves nullable case models") {
        val unionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.string.copy(nullable = true)),
                ReferenceOr.value(Schema.integer.copy(format = "int32")),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Array,
                        nullable = true,
                        items = ReferenceOr.value(Schema.string)
                    )
                )
            )
        )
        val testApi = api.reference("Union", unionSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Union")
                .toModel(NamingContext.reference("Union", SchemaContext.Null), SchemaContext.Write) as Union

            assertEquals(false, result.isNullable)
            assertEquals(true, (result.cases[0].model as Model.Primitive.String).isNullable)
            assertEquals(false, (result.cases[1].model as Model.Primitive.Int).isNullable)

            val nullableCollection = result.cases[2].model as Model.Collection
            assertEquals(true, nullableCollection.isNullable)
            assertEquals(false, nullableCollection.inner.isNullable)
        }
    }

    test("Object property union preserves nested composite collection items") {
        val createModerationRequest = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("input" to ReferenceOr.value(moderationInputUnionSchema())),
            required = listOf("input")
        )
        val testApi = api.reference("CreateModerationRequest", createModerationRequest)

        registry(testApi) {
            val result = ReferenceOr.schema("CreateModerationRequest")
                .toModel(NamingContext.reference("CreateModerationRequest", SchemaContext.Null), SchemaContext.Write)
            val request = assertIs<Model.Object>(result)
            val inputUnion = assertIs<Model.OneOf>(request.properties.getValue("input").model)

            assertEquals(
                NamingContext.reference("CreateModerationRequest", SchemaContext.Null)
                    .nest(NamingContext.ObjectProperty("input")),
                inputUnion.context
            )
            assertEquals(3, inputUnion.cases.size)

            val textInput = assertIs<Model.Primitive.String>(inputUnion.cases[0].model)
            val stringArrayInput = assertIs<Model.Collection>(inputUnion.cases[1].model)
            assertEquals(false, textInput.isNullable)
            val stringArrayItem = assertIs<Model.Primitive.String>(stringArrayInput.inner)
            assertEquals(false, stringArrayItem.isNullable)

            val multimodal = assertIs<Model.Collection>(inputUnion.cases[2].model)
            val itemUnion = assertIs<Model.OneOf>(multimodal.inner)

            assertEquals(
                NamingContext.reference("CreateModerationRequest", SchemaContext.Null)
                    .nest(NamingContext.ObjectProperty("input"))
                    .nest(NamingContext.UnionCase("ImageUrlOrText")),
                itemUnion.context
            )
            assertEquals(
                listOf("image_url", "text"),
                itemUnion.cases.map {
                    val model = assertIs<Model.Object>(it.model)
                    model.context.nested.last().let { nested ->
                        assertIs<NamingContext.UnionCase>(nested).value
                    }
                }
            )
            assertEquals(
                setOf("type", "image_url"),
                assertIs<Model.Object>(itemUnion.cases[0].model).properties.keys
            )
            assertEquals(
                setOf("type", "text"),
                assertIs<Model.Object>(itemUnion.cases[1].model).properties.keys
            )
        }
    }

    test("Multi-value discriminator enum produces tagged custom dispatch") {
        val directory = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("dir", "folder")
                    )
                )
            ),
            required = listOf("type")
        )
        val file = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("file")
                    )
                )
            ),
            required = listOf("type")
        )
        val contentSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("Directory"),
                ReferenceOr.schema("File")
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )
        val testApi = api
            .reference("Directory", directory)
            .reference("File", file)
            .reference("Content", contentSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Content")
                .toModel(NamingContext.reference("Content", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals(UnionDispatch.TaggedCustom("type"), result.dispatch)
            assertEquals(listOf(setOf("dir", "folder"), setOf("file")), result.cases.map { it.discriminatorValues })

            val directoryCase = assertIs<Model.Object>(result.cases[0].model)
            assertEquals(
                NamingContext.reference("Content", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("Directory")),
                directoryCase.context
            )
            assertEquals(setOf("type"), directoryCase.properties.keys)
            assertTrue(directoryCase.properties.getValue("type").isRequired)

            val fileCase = assertIs<Model.Object>(result.cases[1].model)
            assertEquals(
                NamingContext.reference("Content", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("File")),
                fileCase.context
            )
            assertEquals(setOf("type"), fileCase.properties.keys)
            assertTrue(fileCase.properties.getValue("type").isRequired)
        }
    }

    test("Duplicate discriminator tags produce tagged custom dispatch") {
        val first = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("node")
                    )
                )
            ),
            required = listOf("type")
        )
        val second = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("node")
                    )
                )
            ),
            required = listOf("type")
        )
        val unionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("FirstNode"),
                ReferenceOr.schema("SecondNode")
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )
        val testApi = api
            .reference("FirstNode", first)
            .reference("SecondNode", second)
            .reference("NodeUnion", unionSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("NodeUnion")
                .toModel(NamingContext.reference("NodeUnion", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals(UnionDispatch.TaggedCustom("type"), result.dispatch)
            assertEquals(listOf(setOf("node"), setOf("node")), result.cases.map { it.discriminatorValues })

            val firstCase = assertIs<Model.Object>(result.cases[0].model)
            assertEquals(
                NamingContext.reference("NodeUnion", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("FirstNode")),
                firstCase.context
            )

            val secondCase = assertIs<Model.Object>(result.cases[1].model)
            assertEquals(
                NamingContext.reference("NodeUnion", SchemaContext.Null)
                    .nest(NamingContext.UnionCase("SecondNode")),
                secondCase.context
            )
        }
    }

    test("Partial discriminator coverage produces tagged custom dispatch") {
        val cat = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("cat")
                    )
                )
            ),
            required = listOf("type")
        )
        val dog = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(Schema.string)
            ),
            required = listOf("type")
        )
        val petSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(cat),
                ReferenceOr.value(dog)
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )
        val testApi = api.reference("Pet", petSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Pet")
                .toModel(NamingContext.reference("Pet", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals(UnionDispatch.TaggedCustom("type"), result.dispatch)
            assertEquals(listOf(setOf("cat"), emptySet()), result.cases.map { it.discriminatorValues })
        }
    }

    test("Inferred multi-value discriminator produces tagged custom dispatch") {
        val created = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("created", "updated")
                    )
                )
            ),
            required = listOf("type")
        )
        val deleted = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "type" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("deleted")
                    )
                )
            ),
            required = listOf("type")
        )
        val eventSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("CreatedEvent"),
                ReferenceOr.schema("DeletedEvent")
            )
        )
        val testApi = api
            .reference("CreatedEvent", created)
            .reference("DeletedEvent", deleted)
            .reference("Event", eventSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Event")
                .toModel(NamingContext.reference("Event", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals(UnionDispatch.TaggedCustom("type"), result.dispatch)
            assertEquals(listOf(setOf("created", "updated"), setOf("deleted")), result.cases.map { it.discriminatorValues })
        }
    }
}
