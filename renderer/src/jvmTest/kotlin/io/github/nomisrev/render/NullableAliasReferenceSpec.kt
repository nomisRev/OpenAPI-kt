package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A named component that only allows `null` next to another schema is a nullable alias, however it is
 * spelled: `anyOf`/`oneOf: [X, {type: null}]` (in any order) or OpenAPI 3.0's `nullable: true` (also
 * next to `allOf: [{$ref: X}]`); `type: [X, "null"]` is covered by `object/type-array-nullable`.
 * A property, array item or map value referencing it must render nullable (`Alias?`), or `null` on
 * the wire fails to decode. `NullableAliasReferenceRoundTripTest` decodes these goldens.
 */
val nullableAliasReferenceSpec by testSuite {
    modelTest(
        """
        |"NullableAliasHolder": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "anyOfAlias": { "${'$'}ref": "#/components/schemas/AnyOfAlias" },
        |    "nullFirstAnyOfAlias": { "${'$'}ref": "#/components/schemas/NullFirstAnyOfAlias" },
        |    "oneOfAlias": { "${'$'}ref": "#/components/schemas/OneOfAlias" },
        |    "nullableAlias": { "${'$'}ref": "#/components/schemas/NullableAlias" },
        |    "allOfNullableAlias": { "${'$'}ref": "#/components/schemas/AllOfNullableAlias" },
        |    "optionalAnyOfAlias": { "${'$'}ref": "#/components/schemas/AnyOfAlias" },
        |    "anyOfAliases": { "type": "array", "items": { "${'$'}ref": "#/components/schemas/AnyOfAlias" } },
        |    "anyOfAliasMap": { "type": "object", "additionalProperties": { "${'$'}ref": "#/components/schemas/AnyOfAlias" } }
        |  },
        |  "required": [
        |    "anyOfAlias",
        |    "nullFirstAnyOfAlias",
        |    "oneOfAlias",
        |    "nullableAlias",
        |    "allOfNullableAlias",
        |    "anyOfAliases",
        |    "anyOfAliasMap"
        |  ]
        |},
        |"AnyOfAlias": { "anyOf": [{ "type": "string" }, { "type": "null" }] },
        |"NullFirstAnyOfAlias": { "anyOf": [{ "type": "null" }, { "type": "string" }] },
        |"OneOfAlias": { "oneOf": [{ "type": "string" }, { "type": "null" }] },
        |"NullableAlias": { "type": "string", "nullable": true },
        |"AllOfNullableAlias": { "allOf": [{ "${'$'}ref": "#/components/schemas/Owner" }], "nullable": true },
        |"Owner": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": { "name": { "type": "string" } },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "object/nullable-alias-reference"
    )
}
