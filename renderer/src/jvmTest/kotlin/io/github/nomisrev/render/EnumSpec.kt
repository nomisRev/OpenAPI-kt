package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.generateModels

val enumSpec by testSuite {
    modelTest(
        """
        "Sort": {
          "type": "string",
          "description": "The sort order for results.",
          "enum": ["ASC", "DESC"]
        }""".trimMargin(),
        "enum/basic"
    )

    modelTest(
        """
        |"SortSerialName": {
        |  "type": "string",
        |  "enum": [
        |    "very_long_enum_value_1",
        |    "very_long_enum_value_2",
        |    "very_long_enum_value_3",
        |    "very_long_enum_value_4",
        |    "very_long_enum_value_5"
        |  ]
        |}""".trimMargin(),
        "enum/serialname"
    )

    modelTest(
        """
        |"Version": {
        |  "type": "string",
        |  "enum": ["v1"]
        |}
        """.trimMargin(),
        "enum/singlevalue"
    )

    modelTest(
        """
        |"Priority": {
        |  "type": "integer",
        |  "enum": ["1", "2", "3"]
        |}
        """.trimMargin(),
        "enum/int"
    )

    modelTest(
        """
        |"SpecialValues": {
        |  "type": "string",
        |  "enum": ["*", "/", "+1", "-1"]
        |}
        """.trimMargin(),
        "enum/special-chars"
    )

    modelTest(
        """
        |"NullableState": {
        |  "type": "string",
        |  "nullable": true,
        |  "enum": ["on", null, "off"]
        |}
        """.trimMargin(),
        "enum/nullable"
    )

    modelTest(
        """
        |"JsIdentifier": {
        |  "type": "string",
        |  "enum": ["1xx"]
        |}
        """.trimMargin(),
        "enum/jsname"
    )

    // A JSON number or boolean enum is a number or boolean on the wire, not the string kotlinx gives a plain enum.
    modelTest(
        """
        |"Baudrate": {
        |  "type": "integer",
        |  "format": "int32",
        |  "enum": [300, 600, 1200]
        |}
        """.trimMargin(),
        "enum/typed-int"
    )

    // The open enum (`enum | string`) reads and writes JSON strings; a number enum next to a string is an ordinary union.
    modelTest(
        """
        |"Speed": {
        |  "anyOf": [
        |    { "type": "integer", "enum": [300, 600] },
        |    { "type": "string" }
        |  ]
        |}
        """.trimMargin(),
        "enum/typed-in-union"
    )

    // An `integer` without a format is a `Long`, as everywhere else.
    modelTest(
        """
        |"Retries": {
        |  "type": "integer",
        |  "enum": [1, 2, 3]
        |}
        """.trimMargin(),
        "enum/typed-integer-no-format"
    )

    modelTest(
        """
        |"Epoch": {
        |  "type": "integer",
        |  "format": "int64",
        |  "enum": [1, 5000000000]
        |}
        """.trimMargin(),
        "enum/typed-long"
    )

    modelTest(
        """
        |"Scale": {
        |  "type": "number",
        |  "format": "double",
        |  "enum": [0.5, 1.0, 1.5]
        |}
        """.trimMargin(),
        "enum/typed-double"
    )

    modelTest(
        """
        |"Flag": {
        |  "type": "boolean",
        |  "enum": [true, false]
        |}
        """.trimMargin(),
        "enum/typed-boolean"
    )

    modelTest(
        """
        |"Always": {
        |  "type": "boolean",
        |  "enum": [true]
        |}
        """.trimMargin(),
        "enum/typed-boolean-single"
    )

    // Typed enums as properties: referenced, nullable and in a list, plus inline ones, and a default.
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/test": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Meter" } }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Baudrate": { "type": "integer", "enum": [300, 600, 1200] },
              "Meter": {
                "type": "object",
                "properties": {
                  "rate": { "${'$'}ref": "#/components/schemas/Baudrate" },
                  "fallback": { "anyOf": [{ "${'$'}ref": "#/components/schemas/Baudrate" }, { "type": "null" }] },
                  "rates": { "type": "array", "items": { "${'$'}ref": "#/components/schemas/Baudrate" } },
                  "priority": { "type": "integer", "format": "int32", "enum": [1, 2], "default": 2 },
                  "epoch": { "type": "integer", "format": "int64", "enum": [1, 5000000000] },
                  "scale": { "type": "number", "enum": [0.5, 1.5] },
                  "on": { "type": "boolean", "enum": [true, false] }
                },
                "required": ["rate"]
              }
            }
          }
        }
        """.trimIndent(),
        "enum/typed-wire",
    ) { apiTree, config -> apiTree.generateModels(config) }
}
