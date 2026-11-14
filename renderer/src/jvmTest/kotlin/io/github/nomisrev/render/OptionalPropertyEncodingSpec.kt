package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * Optional (not `required`) properties render as `T? = null`. Whether `null` may be sent depends on the
 * schema: an optional property that is not nullable must be omitted when unset, whatever the `Json`
 * configuration of the user's `HttpClient` (Ktor's `DefaultJson` sets `encodeDefaults = true`).
 * `OptionalPropertyEncodingTest` encodes these goldens with different `Json` configurations.
 */
val optionalPropertyEncodingSpec by testSuite {
    modelTest(
        """
        |"OptionalEncoding": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "requiredNotNullable": { "type": "string" },
        |    "optionalNotNullable": { "type": "string" },
        |    "optionalNotNullableWithDefault": { "type": "integer", "format": "int32", "default": 10 },
        |    "optionalNullable": { "type": ["string", "null"] },
        |    "requiredNullable": { "type": ["string", "null"] },
        |    "requiredNullableWithDefault": { "type": ["string", "null"], "default": "hello" },
        |    "nested": {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "id": { "type": "string" },
        |        "note": { "type": "string" },
        |        "maybe": { "type": ["string", "null"] }
        |      },
        |      "required": ["id"]
        |    },
        |    "choice": {
        |      "oneOf": [
        |        {
        |          "type": "object",
        |          "additionalProperties": false,
        |          "properties": {
        |            "kind": { "type": "string", "enum": ["a"] },
        |            "label": { "type": "string" }
        |          },
        |          "required": ["kind"]
        |        },
        |        { "type": "string" }
        |      ]
        |    }
        |  },
        |  "required": ["requiredNotNullable", "requiredNullable", "requiredNullableWithDefault"]
        |}
        """.trimMargin(),
        "object/optional-encoding"
    )

    modelTest(
        """
        |"OptionalEncodingAdditional": {
        |  "type": "object",
        |  "additionalProperties": { "type": "string" },
        |  "properties": {
        |    "id": { "type": "string" },
        |    "note": { "type": "string" }
        |  },
        |  "required": ["id"]
        |}
        """.trimMargin(),
        "object/optional-encoding-additional"
    )

    // Every place a model gets a property: an allOf result, a discriminated object and its subtypes, an object
    // with additional properties (a hand-written serializer), a spec default and a Read/Write split.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/things/extended": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Extended" } } }
                  }
                }
              }
            },
            "/things/pet": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Pet" } } }
                  }
                }
              }
            },
            "/things/withadditional": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/WithAdditional" } } }
                  }
                }
              }
            },
            "/things/account": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Account" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Base": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "id": { "type": "string" },
                  "note": { "type": ["string", "null"] },
                  "limit": { "type": ["integer", "null"], "default": 5 }
                },
                "required": ["id"]
              },
              "Extended": {
                "allOf": [
                  { "$ref": "#/components/schemas/Base" },
                  {
                    "type": "object",
                    "additionalProperties": false,
                    "properties": {
                      "extra": { "type": ["integer", "null"], "format": "int32" }
                    }
                  }
                ]
              },
              "Pet": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "type": { "type": "string", "enum": ["cat"] },
                  "nickname": { "type": ["string", "null"] }
                },
                "required": ["type"],
                "discriminator": {
                  "propertyName": "type",
                  "mapping": {
                    "cat": "#/components/schemas/Pet",
                    "dog": "#/components/schemas/Dog"
                  }
                }
              },
              "Dog": {
                "allOf": [
                  { "$ref": "#/components/schemas/Pet" },
                  {
                    "type": "object",
                    "additionalProperties": false,
                    "properties": {
                      "tricks": { "type": ["array", "null"], "items": { "type": "string" } }
                    }
                  }
                ]
              },
              "WithAdditional": {
                "type": "object",
                "additionalProperties": { "type": "string" },
                "properties": {
                  "id": { "type": "string" },
                  "note": { "type": ["string", "null"] }
                },
                "required": ["id"]
              },
              "Account": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "id": { "type": "string", "readOnly": true },
                  "token": { "type": ["string", "null"], "writeOnly": true },
                  "label": { "type": ["string", "null"] }
                },
                "required": ["id"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/optional-nullable-variants",
    )

    // A model named `Optional` keeps its name: the tri-state type is called `OptionalProperty` then.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/things": {
              "patch": {
                "requestBody": {
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": { "note": { "type": ["string", "null"] } }
                      }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Optional" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Optional": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "value": { "type": ["string", "null"] }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/optional-name-collision",
    )
}
