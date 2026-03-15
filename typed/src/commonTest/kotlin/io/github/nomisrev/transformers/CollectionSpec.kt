package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.ExpectedApi
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.verify
import io.github.nomisrev.verifyAll
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.ResolvedSchema.Value
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.reference
import io.github.nomisrev.verifyFails
import kotlin.collections.emptyList
import kotlin.test.assertEquals

val collectionSpec by testSuite {
    val primitives = (Model.Primitive.all() + Model.FreeFormJson.all()).map { (schema, model) ->
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema))
        val expected = Model.Collection(model, null, null, null, false, null)
        schema expect expected
    }

    verifyAll("Collection with primitive", primitives)

    verifyAll(
        "Collection (primitive) without type: Array",
        primitives.map { (schema, expected) -> schema.copy(type = null) expect expected }
    )

    verifyAll(
        "Collection with referenced primitive",
        (Model.Primitive.all() + Model.FreeFormJson.all()).map { (innerSchema, model) ->
            val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.schema("CollectionItem"))
            val expected = Model.Collection(
                Model.Object.value(NamingContext.Reference("CollectionItem", SchemaContext.Null), model),
                null,
                null,
                null,
                false,
                null
            )
            ExpectedApi(
                schema,
                expected,
                api.reference("CollectionItem", innerSchema),
                listOf(NamingContext.Reference("CollectionItem", SchemaContext.Null))
            )
        }
    ) { schema -> Value(NamingContext.reference("Boop", SchemaContext.Null), schema) }

    verifyAll(
        "Collection with referenced primitive without type: Array",
        (Model.Primitive.all() + Model.FreeFormJson.all()).map { (innerSchema, model) ->
            val schema = Schema(items = ReferenceOr.schema("CollectionItem"))
            val wrapped = Model.Object.value(NamingContext.Reference("CollectionItem", SchemaContext.Null), model)
            val expected = Model.Collection(wrapped, null, null, null, false, null)
            ExpectedApi(
                schema,
                expected,
                api.reference("CollectionItem", innerSchema),
                listOf(NamingContext.Reference("CollectionItem", SchemaContext.Null))
            )
        }
    ) { schema -> Value(NamingContext.reference("Boop", SchemaContext.Null), schema) }

    verifyAll(
        "Referenced Collection with primitive",
        (Model.Primitive.all() + Model.FreeFormJson.all()).map { (innerSchema, model) ->
            val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(innerSchema))
            val expected = Model.Object(
                context = NamingContext.reference("Collection", SchemaContext.Null),
                description = null,
                title = null,
                properties = mapOf(
                    "items" to Model.Object.Property(
                        Model.Collection(model, null, null, null, false, null),
                        true
                    )
                ),
                additionalProperties = false,
                isNullable = false
            )

            ExpectedApi(
                schema,
                expected,
                api.reference("Collection", schema),
                listOf(NamingContext.Reference("Collection", SchemaContext.Null))
            )
        }
    ) { schema -> ResolvedSchema.Reference(NamingContext.Reference("Collection", SchemaContext.Null), schema) }

    verifyAll(
        "Referenced Collection with primitive without type: array",
        (Model.Primitive.all() + Model.FreeFormJson.all()).map { (innerSchema, model) ->
            val schema = Schema(items = ReferenceOr.Value(innerSchema))
            val expected = Model.Object(
                context = NamingContext.reference("Collection", SchemaContext.Null),
                description = null,
                title = null,
                properties = mapOf(
                    "items" to Model.Object.Property(
                        Model.Collection(model, null, null, null, false, null),
                        true
                    )
                ),
                additionalProperties = false,
                isNullable = false
            )
            ExpectedApi(
                schema,
                expected,
                api.reference("Collection", schema),
                listOf(NamingContext.Reference("Collection", SchemaContext.Null))
            )
        }
    ) { schema -> ResolvedSchema.Reference(NamingContext.Reference("Collection", SchemaContext.Null), schema) }

    verifyAll(
        "Referenced Collection preserves nullable items",
        listOf(
            ExpectedApi(
                schema = Schema(
                    type = Type.Basic.Array,
                    items = ReferenceOr.value(Schema(type = Type.Basic.String)),
                    nullable = true
                ),
                model = Model.Object(
                    context = NamingContext.reference("Collection", SchemaContext.Null),
                    description = null,
                    title = null,
                    properties = mapOf(
                        "items" to Model.Object.Property(
                            Model.Collection(
                                Model.Primitive.String(null, null, null, false, null),
                                null,
                                null,
                                null,
                                true,
                                null
                            ),
                            true
                        )
                    ),
                    additionalProperties = false,
                    isNullable = true
                ),
                api = api.reference(
                    "Collection",
                    Schema(
                        type = Type.Basic.Array,
                        items = ReferenceOr.value(Schema(type = Type.Basic.String)),
                        nullable = true
                    )
                ),
                names = listOf(NamingContext.Reference("Collection", SchemaContext.Null))
            )
        )
    ) { schema -> ResolvedSchema.Reference(NamingContext.Reference("Collection", SchemaContext.Null), schema) }

    verify(
        "Default - empty JS array",
        Schema(type = Type.Basic.Array, default = ExampleValue.Single("[]")),
        Model.Collection(
            Model.FreeFormJson(null, null, false, null),
            Model.Default.Value(emptyList()),
            null,
            null,
            false,
            null
        )
    )

    verify(
        "Default - null on nullable array",
        Schema(type = Type.Basic.Array, default = ExampleValue.Single("null"), nullable = true),
        Model.Collection(Model.FreeFormJson(null, null, false, null), Model.Default.Null, null, null, true, null)
    )

    verifyFails<IllegalArgumentException>(
        "Default - null on non-null array",
        Schema(type = Type.Basic.Array, default = ExampleValue.Single("null"), nullable = false),
        "Null default for non-nullable collection."
    )

    verify(
        "Default - value",
        Schema(
            type = Type.Basic.Array,
            items = ReferenceOr.value(Schema(type = Type.Basic.String)),
            default = ExampleValue.Single("A"),
            nullable = false
        ),
        Model.Collection(
            Model.Primitive.String(null, null, null, false, null),
            Model.Default.Value(listOf("A")),
            null,
            null,
            false,
            null
        )
    )

    verify(
        "Default - values",
        Schema(
            type = Type.Basic.Array,
            items = ReferenceOr.value(Schema(type = Type.Basic.String)),
            default = ExampleValue.Multiple(listOf("A", "B")),
            nullable = false
        ),
        Model.Collection(
            Model.Primitive.String(null, null, null, false, null),
            Model.Default.Value(listOf("A", "B")),
            null,
            null,
            false,
            null
        )
    )


    val itemSchema = Schema(
        type = Type.Basic.Object,
        properties = mapOf(
            "id" to ReferenceOr.value(Schema(type = Type.Basic.String)),
            "name" to ReferenceOr.value(Schema(type = Type.Basic.String))
        )
    )

    val s = Schema(type = Type.Basic.Array, items = ReferenceOr.value(itemSchema))
    val item = Model.Object(
        NamingContext.reference("Collection", SchemaContext.Null)
            .nest(NamingContext.ObjectProperty("item")),
        null,
        null,
        mapOf(
            "id" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
            "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false)
        ),
        false,
        false
    )
    val collection = Model.Object(
        NamingContext.reference("Collection", SchemaContext.Null),
        null,
        null,
        mapOf(
            "items" to Model.Object.Property(
                Model.Collection(
                    item,
                    null,
                    null,
                    null,
                    false,
                    null
                ),
                true
            )
        ),
        false,
        false
    )


    test("Referenced Collection with inline context holder") {
        registry(api.reference("Collection", s)) {
            val model = ReferenceOr.schema("Collection")
                .toModel(NamingContext.Reference("Collection", SchemaContext.Null), SchemaContext.Write)
            assertEquals(collection, model)
        }
    }
}
