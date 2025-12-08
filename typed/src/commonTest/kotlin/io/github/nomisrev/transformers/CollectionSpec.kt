package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Eq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.checkAll
import io.github.nomisrev.expect
import io.github.nomisrev.map
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.transformers.collection
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.registry
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.reference
import kotlin.collections.emptyList
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

val collectionSpec by testSuite {

    checkAll("Collection with primitive", (Model.Primitive.all() + Model.FreeFormJson.all()).map { (schema, model) ->
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema))
        val expected = Model.Collection.List(model, null, null, null, false)
        schema expect expected
    }) { schema -> Value(NestedModel, schema).collection(SchemaContext.Input) }

    checkAll(
        "Collection with referenced primitive",
        Model.Primitive.all() + Model.FreeFormJson.all()
    ) { innerSchema, model ->
        val api = api.reference("CollectionItem", innerSchema)
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.schema("CollectionItem"))
        val wrapped = Model.Object.value(NamingContext.Reference("CollectionItem", null), model)
        val expected = Model.Collection.List(wrapped, null, null, null, false)
        val actual = registry(api) { Value(NestedModel, schema).collection(SchemaContext.Input) }
        Eq(expected, actual)
    }

    checkAll(
        "Referenced Collection with primitive:",
        Model.Primitive.all() + Model.FreeFormJson.all()
    ) { schema, model ->
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema))
        val api = api.reference("Collection", Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema)))
        val expected = Model.Collection.List(model, null, null, null, false)
        val actual = registry(api) {
            ResolvedSchema.Reference(NamingContext.Reference("Collection", null), schema)
                .collection(SchemaContext.Input)
        }
        Eq(expected, actual)
    }

    test("Default - empty JS array") {
        val schema = Schema(type = Type.Basic.Array, default = ExampleValue.Single("[]"))
        val expected = Model.Collection.List(
            Model.FreeFormJson(null, null, false),
            Model.Default.Value(emptyList()),
            null,
            null,
            false
        )
        val actual = registry(api) { Value(NestedModel, schema).collection(SchemaContext.Input) }
        assertEquals(expected, actual)
    }

    test("Default - null on nullable array") {
        val schema = Schema(type = Type.Basic.Array, default = ExampleValue.Single("null"), nullable = true)
        val expected =
            Model.Collection.List(Model.FreeFormJson(null, null, false), Model.Default.Null, null, null, true)
        val actual = registry(api) { Value(NestedModel, schema).collection(SchemaContext.Input) }
        assertEquals(expected, actual)
    }

    test("Default - null on non-null array") {
        val schema = Schema(type = Type.Basic.Array, default = ExampleValue.Single("null"), nullable = false)
        val e =
            assertFailsWith<IllegalArgumentException> {
                registry(api) {
                    Value(NestedModel, schema).collection(
                        SchemaContext.Input
                    )
                }
            }
        assertEquals("Null default for non-nullable collection.", e.message)
    }

    test("Default - value") {
        val schema = Schema(
            type = Type.Basic.Array,
            items = ReferenceOr.value(Schema(type = Type.Basic.String)),
            default = ExampleValue.Single("A"),
            nullable = false
        )
        val expected =
            Model.Collection.List(
                Model.Primitive.String(null, null, null, false),
                Model.Default.Value(listOf("A")),
                null,
                null,
                false
            )
        val actual = registry(api) { Value(NestedModel, schema).collection(SchemaContext.Input) }
        assertEquals(expected, actual)
    }

    test("Default - values") {
        val schema = Schema(
            type = Type.Basic.Array,
            items = ReferenceOr.value(Schema(type = Type.Basic.String)),
            default = ExampleValue.Multiple(listOf("A", "B")),
            nullable = false
        )
        val expected =
            Model.Collection.List(
                Model.Primitive.String(null, null, null, false),
                Model.Default.Value(listOf("A", "B")),
                null,
                null,
                false
            )
        val actual = registry(api) { Value(NestedModel, schema).collection(SchemaContext.Input) }
        assertEquals(expected, actual)
    }
}