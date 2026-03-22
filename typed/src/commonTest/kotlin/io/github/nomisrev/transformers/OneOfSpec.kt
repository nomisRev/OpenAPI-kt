package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.verifyAll
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals

val oneOfSpec by testSuite {
    verifyAll(
        "oneOf[]",
        Model.FreeFormJson.all().map { (schema, model) ->
            Schema(
                oneOf = emptyList(),
                description = schema.description,
                nullable = schema.nullable
            ) expect model.copy(constraint = null)
        }
    )

    verifyAll("oneOf[{ type: primitive }]", Model.Primitive.all().map { (schema, model) ->
        Schema(oneOf = listOf(ReferenceOr.value(schema))) expect model
    })

    verifyAll("oneOf[{ type: null }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.NULL),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })

    verifyAll("oneOf[{ nullable: true }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema(nullable = true)),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })
}
