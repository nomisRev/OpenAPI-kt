package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.render.render
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.reference
import kotlin.test.assertEquals

@TestRegistering
fun TestSuite.verify(
    expected: String,
    schema: Schema,
    expectedImports: Set<TypeName> = emptySet()
) {
    fun eq(expected: String, actual: String) =
        if (expected != actual) throw AssertionError(
            """
            |###################
            |###### Actual #####
            |$actual
            |###################
            |###### Expected ###
            |###################
            |$expected
            |###################
        """.trimMargin()
        )
        else Unit

    test(expected) {
        val model = registry(api.reference("Foo", schema)) {
            ReferenceOr.schema("Foo")
                .toModel(NamingContext.Reference("Foo", SchemaContext.Null), SchemaContext.Write)
        }
        val (actual, imports) = renderer { model.render() }
        eq(expected, actual)
        if (expectedImports.isNotEmpty()) {
            assertEquals(expectedImports, imports)
        }
    }
}


@TestRegistering
fun TestSuite.verify(
    expected: String,
    model: Model,
    expectedImports: Set<TypeName> = emptySet()
) {
    fun eq(expected: String, actual: String) =
        if (expected != actual) throw AssertionError(
            """
            |###################
            |###### Actual #####
            |$actual
            |###################
            |###### Expected ###
            |###################
            |$expected
            |###################
        """.trimMargin()
        )
        else Unit

    test(expected) {
        val (actual, imports) = renderer { model.render() }
        eq(expected, actual)
        if (expectedImports != imports) {
            val missing = expectedImports - imports
            val extra = imports - expectedImports
            throw AssertionError("{$missing} are missing from imports. {$extra} are extra.")
        }
    }
}