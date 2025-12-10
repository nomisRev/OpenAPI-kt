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
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.reference
import io.github.nomisrev.verifyFails
import kotlin.collections.emptyList

val collectionSpec by testSuite {
    val name = NamingContext.ObjectProperty("values")
    val primitives = (Model.Primitive.all() + Model.FreeFormJson.all()).map { (schema, model) ->
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema))
        val expected = Model.Collection(model, null, null, null, false)
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
                Model.Object.value(NamingContext.Reference("CollectionItem", null), model),
                null,
                null,
                null,
                false
            )
            ExpectedApi(
                schema,
                expected,
                api.reference("CollectionItem", innerSchema),
                listOf(NamingContext.Reference("CollectionItem", null))
            )
        }
    ) { schema -> Value(NamingContext.Reference("Boop", null), schema) }

    verifyAll(
        "Collection with referenced primitive without type: Array",
        (Model.Primitive.all() + Model.FreeFormJson.all()).map { (innerSchema, model) ->
            val schema = Schema(items = ReferenceOr.schema("CollectionItem"))
            val wrapped = Model.Object.value(NamingContext.Reference("CollectionItem", null), model)
            val expected = Model.Collection(wrapped, null, null, null, false)
            ExpectedApi(
                schema,
                expected,
                api.reference("CollectionItem", innerSchema),
                listOf(NamingContext.Reference("CollectionItem", null))
            )
        }
    ) { schema -> Value(NamingContext.Reference("Boop", null), schema) }

    verifyAll(
        "Referenced Collection with primitive",
        (Model.Primitive.all() + Model.FreeFormJson.all()).map { (innerSchema, model) ->
            val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(innerSchema))
            val expected = Model.Collection(model, null, null, null, false)
            ExpectedApi(
                schema,
                expected,
                api.reference("Collection",schema),
                listOf(NamingContext.Reference("Collection", null))
            )
        }
    ) { schema -> ResolvedSchema.Reference(NamingContext.Reference("Collection", null), schema) }

    verifyAll(
        "Referenced Collection with primitive without type: array",
        (Model.Primitive.all() + Model.FreeFormJson.all()).map { (innerSchema, model) ->
            val schema = Schema(items = ReferenceOr.Value(innerSchema))
            val expected = Model.Collection(model, null, null, null, false)
            ExpectedApi(
                schema,
                expected,
                api.reference("Collection", schema),
                listOf(NamingContext.Reference("Collection", null))
            )
        }
    ) { schema -> ResolvedSchema.Reference(NamingContext.Reference("Collection", null), schema) }

    verify(
        "Default - empty JS array",
        Schema(type = Type.Basic.Array, default = ExampleValue.Single("[]")),
        Model.Collection(
            Model.FreeFormJson(null, null, false),
            Model.Default.Value(emptyList()),
            null,
            null,
            false
        )
    )

    verify(
        "Default - null on nullable array",
        Schema(type = Type.Basic.Array, default = ExampleValue.Single("null"), nullable = true),
        Model.Collection(Model.FreeFormJson(null, null, false), Model.Default.Null, null, null, true)
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
            Model.Primitive.String(null, null, null, false),
            Model.Default.Value(listOf("A")),
            null,
            null,
            false
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
            Model.Primitive.String(null, null, null, false),
            Model.Default.Value(listOf("A", "B")),
            null,
            null,
            false
        )
    )
}