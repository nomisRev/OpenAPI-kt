package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * OpenAPI 3.1 spells a nullable type as a JSON Schema type array: `type: [X, "null"]`.
 * With exactly one non-null type it means the same as OpenAPI 3.0's `type: X, nullable: true`
 * and `anyOf: [X, {type: null}]`, so it must render as the nullable `X?`, never as a
 * one-case sealed interface. `TypeArrayNullableRoundTripTest` decodes these goldens.
 */
val typeArrayNullableSpec by testSuite {
    modelTest(
        """
        |"TypeArrayNullable": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "requiredString": { "type": ["string", "null"] },
        |    "optionalString": { "type": ["string", "null"] },
        |    "nullFirst": { "type": ["null", "string"] },
        |    "anyOfRequiredString": { "anyOf": [{ "type": "string" }, { "type": "null" }] },
        |    "anyOfOptionalString": { "anyOf": [{ "type": "string" }, { "type": "null" }] },
        |    "int32": { "type": ["integer", "null"], "format": "int32" },
        |    "int64": { "type": ["integer", "null"], "format": "int64" },
        |    "number": { "type": ["number", "null"] },
        |    "flag": { "type": ["boolean", "null"] },
        |    "createdAt": { "type": ["string", "null"], "format": "date-time" },
        |    "status": { "type": ["string", "null"], "enum": ["active", "inactive", null] },
        |    "owner": {
        |      "type": ["object", "null"],
        |      "additionalProperties": false,
        |      "properties": { "name": { "type": "string" } },
        |      "required": ["name"]
        |    },
        |    "tags": { "type": ["array", "null"], "items": { "type": "string" } },
        |    "alias": { "${'$'}ref": "#/components/schemas/NullableAlias" },
        |    "anyOfAlias": { "${'$'}ref": "#/components/schemas/AnyOfNullableAlias" },
        |    "single": { "type": ["string"] },
        |    "stringOrInt": { "type": ["string", "integer"] },
        |    "stringOrIntOrNull": { "type": ["string", "integer", "null"] }
        |  },
        |  "required": [
        |    "requiredString",
        |    "nullFirst",
        |    "anyOfRequiredString",
        |    "int32",
        |    "int64",
        |    "number",
        |    "flag",
        |    "createdAt",
        |    "status",
        |    "owner",
        |    "tags",
        |    "alias",
        |    "anyOfAlias",
        |    "single",
        |    "stringOrInt",
        |    "stringOrIntOrNull"
        |  ]
        |},
        |"NullableAlias": { "type": ["string", "null"] },
        |"AnyOfNullableAlias": { "anyOf": [{ "type": "string" }, { "type": "null" }] }
        """.trimMargin(),
        "object/type-array-nullable"
    )

    modelTest(
        """
        |"RequiredNullableMessage": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": { "message": { "type": ["string", "null"] } },
        |  "required": ["message"]
        |}
        """.trimMargin(),
        "object/type-array-nullable-required"
    )

    modelTest(
        """
        |"OptionalNullableMessage": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": { "message": { "type": ["string", "null"] } }
        |}
        """.trimMargin(),
        "object/type-array-nullable-optional"
    )
}
