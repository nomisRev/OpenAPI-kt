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
        |        "note": { "type": "string" }
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
}
