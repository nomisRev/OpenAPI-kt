package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.assertEq
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.reference
import io.github.nomisrev.verifyAll
import kotlin.collections.listOf

val allOfSpec by testSuite {
    verifyAll("allOf[{ type: null }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            allOf = listOf(
                ReferenceOr.value(Schema.NULL),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })

    verifyAll("allOf[{ nullable: true }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            allOf = listOf(
                ReferenceOr.value(Schema(nullable = true)),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })

    val a = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("a" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String))),
    )
    val b = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("b" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String))),
    )
    val ab = Schema(allOf = listOf(ReferenceOr.schema("A"), ReferenceOr.schema("B")))
    val expected = Model.Object(
        NamingContext.Reference("AB", null), null, listOf(
            Model.Object.Property("a", Model.Primitive.String(null, null, null, false), false),
            Model.Object.Property("b", Model.Primitive.String(null, null, null, false), false)
        ), emptySet(), false, false
    )
    test("AB") {
        val registry = Registry(api.reference("A", a).reference("B", b).reference("AB", ab))
        val actual = with(registry) {
            ReferenceOr.schema("AB").toModel(NamingContext.Reference("AB", null), SchemaContext.Write)
        }
        assertEq(expected, actual)
    }
}