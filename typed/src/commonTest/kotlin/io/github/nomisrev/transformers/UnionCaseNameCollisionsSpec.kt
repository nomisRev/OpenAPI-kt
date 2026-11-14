package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

context(registry: Registry)
private fun warningsOfCurrentRegistry(): List<String> = registry.warnings()

/**
 * Two cases of one `oneOf`/`anyOf` must not get one class name. Branches Kotlin cannot tell apart are one case
 * (primitives of one Kotlin type, structurally identical objects), a branch with only `required` is a constraint on
 * the parent object and no case, and different object shapes that would share a name stay separate cases with distinct
 * names: by `title`, by an enum value that tells them apart, else by position with a warning.
 */
val unionCaseNameCollisionsSpec by testSuite {
    val name = NamingContext.path("log")

    suspend fun modelWithWarnings(json: String): Pair<Model, List<String>> =
        registry(api) {
            val model = ReferenceOr.value(Schema.fromJson(json)).toModel(name, SchemaContext.Write)
            model to warningsOfCurrentRegistry()
        }

    fun Model.Union.caseNames(): List<String> =
        cases.map { ((it.model as Model.ContextHolder).context.nested.last() as NamingContext.UnionCase).value }

    val status = { values: String ->
        """{ "type": "object", "properties": { "status": { "type": "string", "enum": [$values] } } }"""
    }

    test("two string branches that differ only by pattern are a String") {
        val [model, warnings] = modelWithWarnings(
            """{ "anyOf": [
                 { "type": "string", "pattern": "^a+$" },
                 { "type": "string", "pattern": "^b+$" } ] }"""
        )
        val string = assertIs<Model.Primitive.String>(model)
        assertNull(string.constraint, "a pattern of one branch must not reject a value of the other")
        assertTrue(warnings.isEmpty(), "$warnings")
    }

    test("string branches that differ by format, length or description are a String") {
        val [model, _] = modelWithWarnings(
            """{ "oneOf": [
                 { "type": "string" },
                 { "type": "string", "format": "datetime" },
                 { "type": "string", "minLength": 3, "maxLength": 9, "description": "short" } ] }"""
        )
        assertNull(assertIs<Model.Primitive.String>(model).constraint)
    }

    test("a union that keeps other cases keeps one case per Kotlin type") {
        val [model, _] = modelWithWarnings(
            """{ "oneOf": [
                 { "type": "string", "pattern": "^a+$" },
                 { "type": "string", "pattern": "^b+$" },
                 { "type": "integer", "format": "int32" } ] }"""
        )
        val cases = assertIs<Model.OneOf>(model).cases.map { it.model }
        assertEquals(2, cases.size)
        assertIs<Model.Primitive.String>(cases[0])
        assertNull(cases[0].let { assertIs<Model.Primitive.String>(it) }.constraint)
        assertIs<Model.Primitive.Int>(cases[1])
    }

    test("the same primitive with the same constraint keeps it") {
        val [model, _] = modelWithWarnings(
            """{ "anyOf": [
                 { "type": "string", "pattern": "^a+$" },
                 { "type": "string", "pattern": "^a+$", "description": "again" } ] }"""
        )
        assertEquals("^a+$", assertIs<Model.Primitive.String>(model).constraint?.pattern)
    }

    test("object branches told apart by an enum value are named after it") {
        val [model, warnings] = modelWithWarnings(
            """{ "oneOf": [${status("\"succeeded\"")}, ${status("\"failed\", \"rejected\"")}] }"""
        )
        val union = assertIs<Model.OneOf>(model)
        assertEquals(listOf("Succeeded", "FailedOrRejected"), union.caseNames())
        assertTrue(warnings.isEmpty(), "$warnings")

        // independent of the declaration order
        val [reversed, _] = modelWithWarnings(
            """{ "oneOf": [${status("\"failed\", \"rejected\"")}, ${status("\"succeeded\"")}] }"""
        )
        assertEquals(listOf("FailedOrRejected", "Succeeded"), assertIs<Model.OneOf>(reversed).caseNames())
    }

    test("object branches with a title are named after it") {
        val [model, warnings] = modelWithWarnings(
            """{ "oneOf": [
                 { "title": "First", "type": "object", "properties": { "value": { "type": "string" } } },
                 { "title": "Second", "type": "object", "properties": { "value": { "type": "integer" } } } ] }"""
        )
        assertEquals(listOf("First", "Second"), assertIs<Model.OneOf>(model).caseNames())
        assertTrue(warnings.isEmpty(), "$warnings")
    }

    test("object shapes that nothing tells apart are numbered by position, with a warning") {
        val [model, warnings] = modelWithWarnings(
            """{ "oneOf": [
                 { "type": "object", "properties": { "value": { "type": "string" } } },
                 { "type": "object", "properties": { "value": { "type": "integer" } } } ] }"""
        )
        assertEquals(listOf("Value1", "Value2"), assertIs<Model.OneOf>(model).caseNames())
        assertEquals(1, warnings.size, "$warnings")
        assertTrue("log" in warnings.single() && "Value1" in warnings.single() && "Value2" in warnings.single())
    }

    test("a union keeps the names it has when they are distinct") {
        val [model, warnings] = modelWithWarnings(
            """{ "oneOf": [
                 { "type": "object", "properties": { "a": { "type": "string" } } },
                 { "type": "object", "properties": { "b": { "type": "string" } } } ] }"""
        )
        assertEquals(listOf("A", "B"), assertIs<Model.OneOf>(model).caseNames())
        assertTrue(warnings.isEmpty(), "$warnings")
    }

    test("structurally identical object branches are one case") {
        val [model, warnings] = modelWithWarnings(
            """{ "oneOf": [
                 { "type": "object", "description": "one", "properties": { "message_uuid": { "type": "string", "format": "uuid" } } },
                 { "type": "object", "description": "two", "properties": { "message_uuid": { "type": "string", "format": "uuid", "description": "x" } } } ] }"""
        )
        val obj = assertIs<Model.Object>(model)
        assertEquals(setOf("message_uuid"), obj.properties.keys)
        assertEquals(name, obj.context, "the single case is the union's own type")
        assertTrue(warnings.isEmpty(), "$warnings")
    }

    test("identical branches next to a different one leave two cases") {
        val [model, _] = modelWithWarnings(
            """{ "oneOf": [
                 { "type": "object", "properties": { "id": { "type": "string" } } },
                 { "type": "object", "description": "again", "properties": { "id": { "type": "string" } } },
                 { "type": "object", "properties": { "id": { "type": "string" }, "other": { "type": "string" } } } ] }"""
        )
        assertEquals(2, assertIs<Model.OneOf>(model).cases.size)
    }

    val gists = """{
        "type": "object", "nullable": true,
        "properties": { "content": { "type": "string" }, "filename": { "type": "string" } },
        "anyOf": [
          { "required": ["content"] },
          { "required": ["filename"] },
          { "type": "object", "maxProperties": 0 } ] }"""

    test("branches that only require properties are a constraint on the parent object") {
        val [model, warnings] = modelWithWarnings(gists)
        val obj = assertIs<Model.Object>(model)
        assertEquals(setOf("content", "filename"), obj.properties.keys)
        assertTrue(obj.isNullable)
        assertEquals(1, warnings.size, "$warnings")
        val warning = warnings.single()
        assertTrue("log" in warning && "not enforced" in warning, warning)
    }

    test("a oneOf of required-only branches directly on an object is a constraint too, and warns") {
        val [model, warnings] = modelWithWarnings(
            """{ "type": "object",
                 "properties": { "email": { "type": "string" }, "phone": { "type": "string" } },
                 "oneOf": [ { "required": ["email"] }, { "required": ["phone"] } ] }"""
        )
        assertEquals(setOf("email", "phone"), assertIs<Model.Object>(model).properties.keys)
        assertEquals(1, warnings.size, "$warnings")
        assertTrue("not enforced" in warnings.single())
    }

    test("required-only branches without a type on an object with properties are a constraint") {
        val [model, _] = modelWithWarnings(
            """{ "properties": { "email": { "type": "string" }, "phone": { "type": "string" } },
                 "anyOf": [ { "required": ["email"] }, { "required": ["phone"] } ] }"""
        )
        assertEquals(setOf("email", "phone"), assertIs<Model.Object>(model).properties.keys)
    }
}
