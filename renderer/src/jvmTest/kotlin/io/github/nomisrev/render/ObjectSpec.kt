package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.generateModels
import io.github.nomisrev.openapi.render

val objectSpec by testSuite {
    modelTest(
        """
        |"User": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "id": { "type": "integer" },
        |    "name": { "type": "string" },
        |    "active": { "type": "boolean" }
        |  },
        |  "required": ["id", "name"]
        |}
        """.trimMargin(),
        "object/dataclass"
    )

    modelTest(
        """
        |"Tag": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "value": { "type": "string" }
        |  },
        |  "required": ["value"]
        |}
        """.trimMargin(),
        "object/valueclass"
    )

    modelTest(
        """
        |"Empty": {
        |  "type": "object",
        |  "additionalProperties": false
        |}
        """.trimMargin(),
        "object/dataobject"
    )

    modelTest(
        """
        |"Nullability": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "requiredNotNullable": { "type": "string" },
        |    "optionalNotNullable": { "type": "string" },
        |    "requiredNullableNoDefault": { "type": "string", "nullable": true },
        |    "requiredNullableWithDefault": { "type": "string", "nullable": true, "default": "hello" }
        |  },
        |  "required": [
        |    "requiredNotNullable",
        |    "requiredNullableNoDefault",
        |    "requiredNullableWithDefault"
        |  ]
        |}
        """.trimMargin(),
        "object/nullability"
    )

    modelTest(
        """
        |"Defaults": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "text": { "type": "string", "default": "hello" },
        |    "count": { "type": "integer", "format": "int32", "default": 42 },
        |    "big": { "type": "integer", "default": 9007199254740991 },
        |    "ratio": { "type": "number", "format": "float", "default": 1.5 },
        |    "score": { "type": "number", "default": 2.75 },
        |    "enabled": { "type": "boolean", "default": true },
        |    "state": {
        |      "type": "string",
        |      "enum": ["on", "off"],
        |      "default": "off"
        |    },
        |    "tags": {
        |      "type": "array",
        |      "items": { "type": "string" },
        |      "default": ["a", "b"]
        |    }
        |  },
        |  "required": ["text", "count", "big", "ratio", "score", "enabled", "state", "tags"]
        |}
        """.trimMargin(),
        "object/defaults"
    )

    modelTest(
        """
        |"SerialNames": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "snake_case": { "type": "string" },
        |    "kebab-case": { "type": "integer" },
        |    "alreadyCamel": { "type": "boolean" }
        |  },
        |  "required": ["snake_case", "kebab-case", "alreadyCamel"]
        |}
        """.trimMargin(),
        "object/serialname"
    )

    modelTest(
        """
        |"Container": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "child": {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "name": { "type": "string" }
        |      },
        |      "required": ["name"]
        |    },
        |    "sort": {
        |      "type": "string",
        |      "enum": ["asc", "desc"]
        |    },
        |    "children": {
        |      "type": "array",
        |      "items": {
        |        "type": "object",
        |        "additionalProperties": false,
        |        "properties": {
        |          "id": { "type": "string" }
        |        },
        |        "required": ["id"]
        |      }
        |    }
        |  },
        |  "required": ["child", "sort", "children"]
        |}
        """.trimMargin(),
        "object/nested"
    )

    modelTest(
        """
        |"Documented": {
        |  "type": "object",
        |  "description": "A documented type.",
        |  "additionalProperties": false,
        |  "properties": {
        |    "id": { "type": "string" }
        |  },
        |  "required": ["id"]
        |}
        """.trimMargin(),
        "object/description"
    )

    modelTest(
        """
        |"MultiLine": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "name": { "type": "string" },
        |    "email": { "type": "integer" },
        |    "age": { "type": "integer", "format": "int32" },
        |    "longername": { "type": "number", "nullable": true },
        |    "longername2": { "type": "number", "format": "float" },
        |    "longer_name_3": { "type": "string", "format": "uuid" },
        |    "longername4": { "type": "string", "format": "date-time" }
        |  },
        |  "required": ["age", "longername"]
        |}
        """.trimMargin(),
        "object/multi-line"
    )

    modelTest(
        """
        |"PrimitiveImports": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "date": { "type": "string", "format": "date" },
        |    "dateTime": { "type": "string", "format": "date-time" },
        |    "uuid": { "type": "string", "format": "uuid" },
        |    "json": {},
        |    "jsonArray": {
        |      "type": "array",
        |      "items": {}
        |    },
        |    "jsonObject": {
        |      "type": "object",
        |      "additionalProperties": true
        |    }
        |  },
        |  "required": ["date", "dateTime", "uuid", "json", "jsonArray", "jsonObject"]
        |}
        """.trimMargin(),
        "object/primitive-imports"
    )

    modelTest(
        """
        |"AdditionalBoolean": {
        |  "type": "object",
        |  "additionalProperties": true,
        |  "properties": {
        |    "name": { "type": "string" },
        |    "age": { "type": "integer", "format": "int32" }
        |  },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "object/additional-boolean"
    )

    modelTest(
        """
        |"AdditionalSchema": {
        |  "type": "object",
        |  "additionalProperties": { "type": "string" },
        |  "properties": {
        |    "name": { "type": "string" }
        |  },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "object/additional-schema"
    )

    modelTest(
        """
        |"AdditionalComplex": {
        |  "type": "object",
        |  "additionalProperties": {
        |    "type": "object",
        |    "additionalProperties": false,
        |    "properties": {
        |      "enabled": { "type": "boolean" }
        |    },
        |    "required": ["enabled"]
        |  },
        |  "properties": {
        |    "name": { "type": "string" }
        |  },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "object/additional-complex"
    )

    modelTest(
        """
        |"AdditionalSerializer": {
        |  "type": "object",
        |  "additionalProperties": { "type": "integer", "format": "int32" },
        |  "properties": {
        |    "snake_case": { "type": "string" },
        |    "optionalFlag": { "type": "boolean" },
        |    "requiredNullable": { "type": "string", "nullable": true }
        |  },
        |  "required": ["snake_case", "requiredNullable"]
        |}
        """.trimMargin(),
        "object/additional-serializer"
    )

    modelTest(
        """
        |"SignedNames": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "+1":     { "type": "integer" },
        |    "-1":     { "type": "integer" },
        |    "+count": { "type": "integer" },
        |    "-offset":{ "type": "integer" }
        |  },
        |  "required": ["+1", "-1", "+count", "-offset"]
        |}
        """.trimMargin(),
        "object/signed-names"
    )

    // ISSUE_DO_EMPTY_DATA_OBJECT_ALL_READONLY + ISSUE_DO_WRITE_VARIANT_ZERO_WRITABLE_FIELDS:
    // Schema whose ALL properties are readOnly.
    //   - Plain/Write variant must render as `data class()` (zero params), NOT `data object`
    //   - Read variant must render normally as a data class with all fields
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Test API", "version": "0.0.1" },
          "paths": {
            "/items": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "${'$'}ref": "#/components/schemas/AllReadOnly" }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/AllReadOnly" }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "AllReadOnly": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "id":           { "type": "string", "readOnly": true },
                  "presentation": { "type": "string", "readOnly": true },
                  "pattern":      { "type": "string", "readOnly": true }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "object/all-readonly-variants"
    ) { apiTree, config -> apiTree.render(config) }

    // ISSUE_DO_VALUE_CLASS_FROM_READONLY_STRIPPING:
    // Schema with multiple properties but only one is writable.
    //   - Write variant must render as `data class` (NOT `@JvmInline value class`), since
    //     the schema had multiple properties before stripping.
    //   - Read variant includes all fields and renders normally.
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Test API", "version": "0.0.1" },
          "paths": {
            "/locales": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "${'$'}ref": "#/components/schemas/LocaleDescriptor" }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/LocaleDescriptor" }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "LocaleDescriptor": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "id":        { "type": "string",  "readOnly": true },
                  "locale":    { "type": "string",  "readOnly": true },
                  "language":  { "type": "string",  "readOnly": true },
                  "community": { "type": "boolean", "readOnly": true },
                  "name":      { "type": "string" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "object/single-writable-field-variants"
    ) { apiTree, config -> apiTree.render(config) }

    // ISSUE_DO_PLAIN_MODEL_LEAKS_READ_NESTED_TYPE:
    // A plain (non-suffixed) schema that references a discriminated type which has both
    // Read and Write variants. The plain schema's property should resolve to the Write variant
    // (or the non-suffixed variant), NOT leak the Read suffix.
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Test API", "version": "0.0.1" },
          "paths": {
            "/issues/{id}": {
              "get": {
                "parameters": [
                  {
                    "name": "id",
                    "in": "path",
                    "required": true,
                    "schema": { "type": "string" }
                  }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/IssueKey" }
                      }
                    }
                  }
                }
              }
            },
            "/folders/{id}": {
              "post": {
                "parameters": [
                  {
                    "name": "id",
                    "in": "path",
                    "required": true,
                    "schema": { "type": "string" }
                  }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "${'$'}ref": "#/components/schemas/IssueFolder" }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/IssueFolder" }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "IssueKey": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "id":              { "type": "string" },
                  "project":         { "${'$'}ref": "#/components/schemas/IssueFolder" },
                  "numberInProject": { "type": "integer" }
                }
              },
              "IssueFolder": {
                "type": "object",
                "additionalProperties": false,
                "discriminator": {
                  "propertyName": "${'$'}type",
                  "mapping": {
                    "Project":     "#/components/schemas/Project",
                    "IssueFolder": "#/components/schemas/IssueFolder"
                  }
                },
                "properties": {
                  "id":     { "type": "string", "readOnly": true },
                  "${'$'}type": { "type": "string" }
                }
              },
              "Project": {
                "allOf": [
                  { "${'$'}ref": "#/components/schemas/IssueFolder" },
                  {
                    "type": "object",
                    "additionalProperties": false,
                    "properties": {
                      "shortName": { "type": "string" }
                    }
                  }
                ]
              }
            }
          }
        }
        """.trimIndent(),
        "object/plain-model-no-read-suffix"
    ) { apiTree, config -> apiTree.render(config) }

    // Read/Write variant: schema used as both request body (Write) and response (Read)
    // Both contexts exist → Read/Write suffixes are applied
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Test API", "version": "0.0.1" },
          "paths": {
            "/users": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "${'$'}ref": "#/components/schemas/User" }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/User" }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "User": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "id":        { "type": "integer", "readOnly": true },
                  "name":      { "type": "string" },
                  "createdAt": { "type": "string", "format": "date-time", "readOnly": true }
                },
                "required": ["id", "name", "createdAt"]
              }
            }
          }
        }
        """.trimIndent(),
        "object/read-write-variants"
    ) { apiTree, config -> apiTree.render(config) }

    // Single-context: schema only used as a response → no suffix applied
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Test API", "version": "0.0.1" },
          "paths": {
            "/tags": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/Tag" }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Tag": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "id":   { "type": "integer", "readOnly": true },
                  "name": { "type": "string" }
                },
                "required": ["id", "name"]
              }
            }
          }
        }
        """.trimIndent(),
        "object/read-only-single-context"
    ) { apiTree, config -> apiTree.generateModels(config) }
}
