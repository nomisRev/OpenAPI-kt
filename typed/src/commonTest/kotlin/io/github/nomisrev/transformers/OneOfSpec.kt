package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.checkAll
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.toModel

val oneOfSpec by testSuite {
    val oneOf = Model.Primitive.all().map { (schema, model) ->
        Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.NULL),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    }

    checkAll("oneOf[{ type: null }, { type: primitive }", oneOf) {
        ResolvedSchema.Value(NamingContext.ObjectProperty("test"), it)
            .toModel(SchemaContext.Input)
    }

    val singleAnyOf = Model.Primitive.all().map { (schema, model) ->
        Schema(oneOf = listOf(ReferenceOr.value(schema))) expect model
    }

    checkAll("oneOf[{ type: primitive }]", singleAnyOf) {
        ResolvedSchema.Value(NamingContext.ObjectProperty("test"), it)
            .toModel(SchemaContext.Input)
    }
}