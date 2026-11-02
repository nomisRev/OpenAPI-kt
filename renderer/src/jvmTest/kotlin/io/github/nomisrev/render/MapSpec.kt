package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A `type: object` schema with only `additionalProperties: <schema>` is a map: a JSON object
 * `{"key": value}` on the wire, so it must render as `Map<String, V>`, never as `List<V>`.
 */
val mapSpec by testSuite {
    modelTest(
        """
        |"Labels": {
        |  "type": "object",
        |  "additionalProperties": { "type": "string" }
        |}
        """.trimMargin(),
        "map/top-level-primitive"
    )

    modelTest(
        """
        |"Pets": {
        |  "type": "object",
        |  "additionalProperties": { "${'$'}ref": "#/components/schemas/Pet" }
        |},
        |"Pet": {
        |  "type": "object",
        |  "properties": {
        |    "name": { "type": "string" },
        |    "age": { "type": "integer", "format": "int32" }
        |  },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "map/top-level-ref-values"
    )

    modelTest(
        """
        |"Holder": {
        |  "type": "object",
        |  "properties": {
        |    "inline": {
        |      "type": "object",
        |      "additionalProperties": { "type": "integer", "format": "int32" }
        |    },
        |    "labels": { "${'$'}ref": "#/components/schemas/Labels" }
        |  },
        |  "required": ["inline", "labels"]
        |},
        |"Labels": {
        |  "type": "object",
        |  "additionalProperties": { "type": "string" }
        |}
        """.trimMargin(),
        "map/property"
    )

    modelTest(
        """
        |"Setting": {
        |  "anyOf": [
        |    { "type": "string" },
        |    {
        |      "type": "object",
        |      "additionalProperties": { "type": "integer", "format": "int32" }
        |    }
        |  ]
        |}
        """.trimMargin(),
        "map/union-case"
    )
}
