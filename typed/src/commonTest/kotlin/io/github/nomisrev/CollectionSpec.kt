package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.collection
import io.github.nomisrev.openapi.ctx
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import kotlin.collections.emptyList
import kotlin.test.assertEquals

val collectionSpec by testSuite {
    (Model.Primitive.all() + Model.FreeFormJson.all()).forEach { (schema, model) ->
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema))
        val expected = Model.Collection.List(model, emptyList(), null, null, false)
        test("Collection with primitive: $schema") {
            val actual = ctx(api) { Value(schema).collection(SchemaContext.Input) }
            assertEquals(actual, expected)
        }
    }

    (Model.Primitive.all() + Model.FreeFormJson.all()).forEach { (schema, model) ->
        val api = api.withReference("CollectionItem", schema)
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.schema("CollectionItem"))
        val wrapped = Model.Object(
            context = NamingContext.Reference("CollectionItem", null),
            description = model.description,
            properties = listOf(Model.Object.Property("value", model, true, null)),
            inline = emptySet(),
            isNullable = model.isNullable
        )

        val expected = Model.Collection.List(wrapped, emptyList(), null, null, false)
        test("Collection with referenced primitive: $schema") {
            val actual = ctx(api) { Value(schema).collection(SchemaContext.Input) }
            assertEquals(actual, expected)
        }
    }

    (Model.Primitive.all() + Model.FreeFormJson.all()).forEach { (schema, model) ->
        val schema = Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema))
        val api = api.withReference("Collection", Schema(type = Type.Basic.Array, items = ReferenceOr.Value(schema)))
        val expected = Model.Collection.List(model, emptyList(), null, null, false)

        test("Referenced Collection with primitive: $schema") {
            val actual = ctx(api) {
                ResolvedSchema.Reference(NamingContext.Reference("Collection", null), schema)
                    .collection(SchemaContext.Input)
            }
            assertEquals(actual, expected)
        }
    }
}