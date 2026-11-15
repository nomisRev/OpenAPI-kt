package io.github.nomisrev.openapi.registry

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals

/**
 * `readOnly` or `writeOnly` next to the `$ref` of a property says when the property is sent. It makes the schema that
 * has the property one with a request and a response shape, and says nothing about the schema it refers to, which has
 * two shapes only when its own content does.
 */
val AnnotatedReferenceSpec by testSuite {
    fun spec(statusProperties: String) = """
        {
          "openapi": "3.0.0",
          "info": { "title": "Api", "version": "1" },
          "paths": {
            "/scans": {
              "get": {
                "responses": { "200": { "description": "ok",
                  "content": { "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Scan" } } } } }
              },
              "post": {
                "requestBody": { "required": true,
                  "content": { "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Scan" } } } },
                "responses": { "204": { "description": "ok" } }
              }
            }
          },
          "components": { "schemas": {
            "Scan": { "type": "object", "properties": {
              "name": { "type": "string" },
              "status": { "${'$'}ref": "#/components/schemas/Status", "readOnly": true },
              "secret": { "${'$'}ref": "#/components/schemas/Secret", "writeOnly": true } } },
            "Status": { "type": "object", "properties": { $statusProperties } },
            "Secret": { "type": "object", "properties": { "token": { "type": "string" } } }
          } }
        }
    """.trimIndent()

    suspend fun models(statusProperties: String): List<Pair<NamingContext.Reference, Set<String>>> =
        OpenAPI.fromJson(spec(statusProperties)).toApiTree().models.map { model ->
            model as Model.Object
            (model.context.head as NamingContext.Reference) to model.properties.keys
        }

    test("an annotated reference to a schema with one shape gives that schema once") {
        assertEquals(
            setOf(
                NamingContext.Reference("Scan", SchemaContext.Read) to setOf("name", "status"),
                NamingContext.Reference("Scan", SchemaContext.Write) to setOf("name", "secret"),
                NamingContext.Reference("Status", SchemaContext.Null) to setOf("code"),
                NamingContext.Reference("Secret", SchemaContext.Null) to setOf("token"),
            ),
            models(""" "code": { "type": "integer" } """).toSet(),
        )
    }

    test("no model is listed twice") {
        val models = models(""" "code": { "type": "integer" } """)
        assertEquals(models.distinct(), models)
    }

    test("an annotated reference to a schema with two shapes takes the shape of where it is used") {
        assertEquals(
            setOf(
                NamingContext.Reference("Scan", SchemaContext.Read) to setOf("name", "status"),
                NamingContext.Reference("Scan", SchemaContext.Write) to setOf("name", "secret"),
                NamingContext.Reference("Status", SchemaContext.Read) to setOf("code", "id"),
                NamingContext.Reference("Secret", SchemaContext.Null) to setOf("token"),
            ),
            models(""" "code": { "type": "integer" }, "id": { "type": "string", "readOnly": true } """).toSet(),
        )
    }
}
