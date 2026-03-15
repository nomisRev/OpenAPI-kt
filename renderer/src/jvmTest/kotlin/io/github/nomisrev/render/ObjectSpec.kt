package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

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
}
