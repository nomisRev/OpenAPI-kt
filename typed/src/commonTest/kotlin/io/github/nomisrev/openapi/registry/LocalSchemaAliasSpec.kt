package io.github.nomisrev.openapi.registry

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.routes.toRoutes
import io.ktor.http.ContentType
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

val LocalSchemaAliasSpec by testSuite {
    test("local aliases resolve through multiple components") {
        val original = OpenAPI.fromJson(bbcAliasDocument)
        val api = original.copy(components = original.components.copy(
            schemas = original.components.schemas + ("alias" to ReferenceOr.schema("broadcast_of")),
        ))
        Registry(api).use { registry ->
            val expected = assertIs<ReferenceOr.Value<*>>(api.components.schemas.getValue("pidReference")).value
            assertEquals(expected, registry.scope().peek("#/components/schemas/alias"))
        }
    }

    test("cyclic local aliases fail with a diagnostic instead of looping") {
        val original = OpenAPI.fromJson(bbcAliasDocument)
        val api = original.copy(components = original.components.copy(
            schemas = mapOf(
                "first" to ReferenceOr.schema("second"),
                "second" to ReferenceOr.schema("first"),
            ),
        ))
        Registry(api).use { registry ->
            val failure = assertFailsWith<IllegalStateException> {
                registry.scope().peek("#/components/schemas/first")
            }
            assertTrue(failure.message.orEmpty().contains("Cyclic schema aliases"))
            assertTrue(failure.message.orEmpty().contains("#/components/schemas/first"))
            assertTrue(failure.message.orEmpty().contains("#/components/schemas/second"))
        }
    }

    test("BBC broadcast_of alias resolves to the local pidReference schema") {
        val api = OpenAPI.fromJson(bbcAliasDocument)
        Registry(api).use { registry ->
            val expected = assertIs<ReferenceOr.Value<*>>(api.components.schemas.getValue("pidReference")).value
            assertEquals(expected, registry.scope().peek("#/components/schemas/broadcast_of"))
        }
    }

    test("BBC broadcast array items can reference a local schema alias") {
        val api = OpenAPI.fromJson(bbcAliasDocument)
        Registry(api).use { registry ->
            val model = context(registry) {
                ReferenceOr.schema("broadcast").toModel(
                    NamingContext.Reference("broadcast", SchemaContext.Null), SchemaContext.Read,
                )
            }
            val broadcast = assertIs<Model.Object>(model)
            val items = assertIs<Model.Collection>(broadcast.properties.getValue("broadcast_of").model)
            val item = assertIs<Model.Object>(items.inner)
            assertEquals(setOf("href", "pid", "position", "result_type"), item.properties.keys)
            assertEquals(setOf("pid", "result_type"), item.properties.filterValues { it.isRequired }.keys)
        }
    }

    test("papiNet GetOrder alias resolves to the local composed Order schema") {
        val api = OpenAPI.fromJson(papiNetAliasDocument)
        Registry(api).use { registry ->
            val expected = assertIs<ReferenceOr.Value<*>>(api.components.schemas.getValue("Order")).value
            assertEquals(expected, registry.scope().peek("#/components/schemas/GetOrder"))
        }
    }

    test("papiNet GetOrder response alias retains Order properties and required fields") {
        val api = OpenAPI.fromJson(papiNetAliasDocument)
        Registry(api).use { registry ->
            val model = context(registry) {
                ReferenceOr.schema("GetOrder").toModel(
                    NamingContext.Reference("GetOrder", SchemaContext.Null), SchemaContext.Read,
                )
            }
            val order = assertIs<Model.Object>(model)
            assertEquals(
                setOf("id", "numberOfLineItems", "orderNumber", "orderStatus", "links", "orderLineItems"),
                order.properties.keys,
            )
            assertEquals(
                setOf("id", "numberOfLineItems", "orderNumber", "orderStatus"),
                order.properties.filterValues { it.isRequired }.keys,
            )
            assertIs<Model.Collection>(order.properties.getValue("orderLineItems").model)
        }
    }

    // A bare `$ref` component, `Alias: {$ref: X}`, used as a request body. Request bodies resolved the component
    // without following the alias and failed with `Remote schemas not supported yet.`, so no client could be
    // generated for a spec using one, while the same alias as a property or response already resolved through `peek`.
    suspend fun routes(api: OpenAPI): List<Route> = Registry(api).use { registry -> with(registry) { api.toRoutes() } }

    suspend fun bodyModel(aliases: String, body: String, contentType: String = "application/json"): Route.Body =
        routes(OpenAPI.fromJson(bareAliasDocument(aliases, body, contentType)))
            .single().body.let { requireNotNull(it) { "Expected a request body" } }
            .types.getValue(ContentType.parse(contentType))

    val bareOwner = $$""""BareOwner": { "$ref": "#/components/schemas/Owner" }"""

    test("a JSON request body that is a bare \$ref alias models the target under the alias name") {
        val body = assertIs<Route.Body.SetBody>(bodyModel(bareOwner, "BareOwner"))
        val model = assertIs<Model.Object>(body.type)
        assertEquals(NamingContext.reference("BareOwner", SchemaContext.Null), model.context)
        assertEquals(setOf("name"), model.properties.keys)
        assertTrue(!model.isNullable)
    }

    test("a JSON request body that is a bare \$ref alias of a primitive is the alias value wrapper") {
        val body = assertIs<Route.Body.SetBody>(
            bodyModel($$""""BareLabel": { "$ref": "#/components/schemas/Label" }""", "BareLabel")
        )
        val model = assertIs<Model.Object>(body.type)
        assertEquals(NamingContext.reference("BareLabel", SchemaContext.Null), model.context)
        assertIs<Model.Primitive.String>(model.properties.values.single().model)
    }

    test("a JSON request body through an alias chain A -> B -> Owner models Owner under A's name") {
        val aliases = $$""""ChainOwner": { "$ref": "#/components/schemas/BareOwner" }, $$bareOwner"""
        val body = assertIs<Route.Body.SetBody>(bodyModel(aliases, "ChainOwner"))
        val model = assertIs<Model.Object>(body.type)
        assertEquals(NamingContext.reference("ChainOwner", SchemaContext.Null), model.context)
        assertEquals(setOf("name"), model.properties.keys)
    }

    test("a JSON request body aliasing a nullable alias is the nullable alias type") {
        val aliases = $$"""
            "NullableChainOwner": { "$ref": "#/components/schemas/NullableOwner" },
            "NullableOwner": { "anyOf": [{ "$ref": "#/components/schemas/Owner" }, { "type": "null" }] }
        """
        val body = assertIs<Route.Body.SetBody>(bodyModel(aliases, "NullableChainOwner"))
        val model = assertIs<Model.Object>(body.type)
        assertEquals(NamingContext.reference("NullableChainOwner", SchemaContext.Null), model.context)
        assertTrue(model.isNullable, "should be nullable: $model")
    }

    test("a multipart request body that is a bare \$ref alias uses the target's properties") {
        val body = assertIs<Route.Body.Multipart.Value>(bodyModel(bareOwner, "BareOwner", "multipart/form-data"))
        assertEquals(listOf("name"), body.parameters.map { it.name })
    }

    test("a form-urlencoded request body that is a bare \$ref alias uses the target's properties") {
        val body = assertIs<Route.Body.FormUrlEncoded>(
            bodyModel(bareOwner, "BareOwner", "application/x-www-form-urlencoded")
        )
        assertEquals(listOf("name"), body.parameters.map { it.name })
        assertTrue(body.isSupported)
    }

    test("a request body through cyclic aliases fails with a diagnostic instead of looping") {
        val aliases = $$"""
            "First": { "$ref": "#/components/schemas/Second" },
            "Second": { "$ref": "#/components/schemas/First" }
        """
        val failure = assertFailsWith<IllegalStateException> { bodyModel(aliases, "First") }
        assertTrue(failure.message.orEmpty().contains("Cyclic schema aliases"), "${failure.message}")
        assertTrue(failure.message.orEmpty().contains("#/components/schemas/First"), "${failure.message}")
        assertTrue(failure.message.orEmpty().contains("#/components/schemas/Second"), "${failure.message}")
    }
}

// Reduced from API Guru BBC Nitro 1.0.0, corpus key 0805356d6bc5b6ebb665.
// Only broadcast.broadcast_of is retained from broadcast; its array, alias, and
// all transitively referenced component definitions are copied unchanged.
private val bbcAliasDocument = $$"""
{
  "openapi": "3.0.0",
  "info": {
    "title": "BBC Nitro API",
    "version": "1.0.0"
  },
  "paths": {},
  "components": {
    "schemas": {
      "broadcast": {
        "type": "object",
        "additionalProperties": false,
        "properties": {
          "broadcast_of": {
            "items": {
              "$ref": "#/components/schemas/broadcast_of"
            },
            "type": "array"
          }
        }
      },
      "broadcast_of": {
        "$ref": "#/components/schemas/pidReference"
      },
      "pidReference": {
        "additionalProperties": false,
        "properties": {
          "href": {
            "type": "string"
          },
          "pid": {
            "$ref": "#/components/schemas/pid"
          },
          "position": {
            "type": "integer"
          },
          "result_type": {
            "type": "string"
          }
        },
        "required": [
          "pid",
          "result_type"
        ],
        "type": "object"
      },
      "pid": {
        "pattern": "([a-z0-9\\.\\-]+|.*PID.*)",
        "type": "string"
      }
    }
  }
}
""".trimIndent()

// Reduced from API Guru papiNet Order Status, corpus key 0bf408c989cb6d5cdf61.
// GET /orders/{orderId} returns GetOrder. The alias and its complete transitive
// component definitions (including Order's allOf) are copied unchanged.
private val papiNetAliasDocument = $$"""
{
  "openapi": "3.0.3",
  "info": {
    "title": "papiNet API",
    "version": "1.0.0"
  },
  "paths": {},
  "components": {
    "schemas": {
      "GetOrder": {
        "$ref": "#/components/schemas/Order"
      },
      "Order": {
        "allOf": [
          {
            "$ref": "#/components/schemas/OrderHeader"
          },
          {
            "properties": {
              "links": {
                "$ref": "#/components/schemas/PaginationLinks"
              },
              "orderLineItems": {
                "items": {
                  "$ref": "#/components/schemas/OrderLineItem"
                },
                "type": "array"
              }
            }
          }
        ]
      },
      "OrderHeader": {
        "properties": {
          "id": {
            "format": "uuid",
            "type": "string"
          },
          "numberOfLineItems": {
            "minimum": 0,
            "type": "integer"
          },
          "orderNumber": {
            "type": "string"
          },
          "orderStatus": {
            "enum": [
              "Active",
              "Cancelled",
              "Completed"
            ],
            "type": "string"
          }
        },
        "required": [
          "id",
          "orderNumber",
          "orderStatus",
          "numberOfLineItems"
        ],
        "type": "object"
      },
      "PaginationLinks": {
        "properties": {
          "first": {
            "properties": {
              "href": {
                "type": "string"
              }
            },
            "type": "object"
          },
          "last": {
            "properties": {
              "href": {
                "type": "string"
              }
            },
            "type": "object"
          },
          "next": {
            "properties": {
              "href": {
                "type": "string"
              }
            },
            "type": "object"
          },
          "prev": {
            "properties": {
              "href": {
                "type": "string"
              }
            },
            "type": "object"
          }
        },
        "required": [
          "next"
        ],
        "type": "object"
      },
      "OrderLineItem": {
        "properties": {
          "changeable": {
            "type": "boolean"
          },
          "id": {
            "format": "uuid",
            "type": "string"
          },
          "orderLineItemNumber": {
            "minimum": 0,
            "type": "number"
          },
          "orderLineItemStatus": {
            "enum": [
              "Cancelled",
              "Completed",
              "Confirmed",
              "Pending",
              "ProductionCompleted",
              "ShipmentCompleted"
            ],
            "type": "string"
          },
          "quantities": {
            "items": {
              "properties": {
                "quantityContext": {
                  "enum": [
                    "Confirmed",
                    "Invoiced",
                    "Ordered",
                    "Produced",
                    "Shipped"
                  ],
                  "type": "string"
                },
                "quantityType": {
                  "enum": [
                    "Area",
                    "Count",
                    "GrossWeight",
                    "Length",
                    "NetNetWeight",
                    "NetWeight",
                    "NominalWeight",
                    "TareWeight"
                  ],
                  "type": "string"
                },
                "quantityUOM": {
                  "enum": [
                    "Bale",
                    "Box",
                    "Centimeter",
                    "Decimeter",
                    "Foot",
                    "Gram",
                    "HundredPounds",
                    "Inch",
                    "Kilogram",
                    "Kilometer",
                    "Meter",
                    "MetricTon",
                    "Millimeter",
                    "Package",
                    "PalletUnit",
                    "Piece",
                    "Pound",
                    "PulpUnit",
                    "Ream",
                    "Reel",
                    "Sheet",
                    "ShortTon",
                    "Skid",
                    "SquareDecimeter",
                    "SquareFoot",
                    "SquareInch",
                    "SquareMeter",
                    "ThousandPieces",
                    "ThousandSquareCentimeters",
                    "ThousandSquareFeet",
                    "ThousandSquareInches",
                    "Yard"
                  ],
                  "type": "string"
                },
                "quantityValue": {
                  "type": "number"
                }
              },
              "type": "object"
            },
            "type": "array"
          }
        },
        "required": [
          "id",
          "orderLineItemNumber",
          "orderLineItemStatus"
        ],
        "type": "object"
      }
    }
  }
}
""".trimIndent()

private fun bareAliasDocument(aliases: String, body: String, contentType: String): String = $$"""
{
  "openapi": "3.1.0",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": {
    "/owners": {
      "put": {
        "requestBody": {
          "required": true,
          "content": { "$$contentType": { "schema": { "$ref": "#/components/schemas/$$body" } } }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    }
  },
  "components": {
    "schemas": {
      "Owner": {
        "type": "object",
        "additionalProperties": false,
        "properties": { "name": { "type": "string" } },
        "required": ["name"]
      },
      "Label": { "type": "string" },
      $$aliases
    }
  }
}
""".trimIndent()
