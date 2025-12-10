package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.assertEq
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.reference
import kotlin.test.assertEquals

val nestedSpec by testSuite {
    test("Referenced Nested Object") {
        val topInt = NamingContext.Reference("TopInt", null)
        val api = api.reference("TopInt", Schema.integer)
        val intM = Model.Primitive.Long(null, null, null, false)
        val intObj = Model.Object.value(topInt, intM)
        val expected = Model.Object(
            NamingContext.RouteParam("get", "getBy"),
            null,
            listOf(
                Model.Object.Property("prop", intObj, false)
            ),
            emptySet(),
            false,
            false
        )
        val registry = Registry(api)
        val actual = with(registry) {
            ReferenceOr.value(
                Schema(
                    type = Type.Basic.Object,
                    properties = mapOf("prop" to schema("TopInt"))
                )
            ).toModel(NamingContext.RouteParam("get", "getBy"), SchemaContext.Input)
        }
        assertEq(expected, actual)
        assertEquals(setOf(topInt), registry.names())
    }
}