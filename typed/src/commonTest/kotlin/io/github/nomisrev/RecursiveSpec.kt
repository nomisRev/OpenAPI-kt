package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry
import io.github.nomisrev.openapi.resolve
import io.github.nomisrev.openapi.toModel


val recursiveSpec by testSuite {
    val recursiveAnchor = Schema(
        recursiveAnchor = true,
        type = Schema.Type.Basic.Object,
        properties = mapOf(
            "filters" to ReferenceOr.value(
                Schema(anyOf = listOf(
                    ReferenceOr.value(Schema(type = Schema.Type.Basic.Array, items = ReferenceOr.Reference("#"))),
                    ReferenceOr.value(Schema(type = Schema.Type.Basic.Null))
                ))
            )
        ),
        required = listOf("filters")
    )

    test("recursive anchor") {
        val actual = with(Registry(api.reference("Root", recursiveAnchor))) {
            ReferenceOr.schema("Root")
                .toModel(NamingContext.Reference("Root", null), SchemaContext.Input)
        }
        println(actual)
    }
}