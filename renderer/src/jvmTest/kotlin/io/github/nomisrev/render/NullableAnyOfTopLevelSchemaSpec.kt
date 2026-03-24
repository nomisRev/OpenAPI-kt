package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val nullableAnyOfTopLevelSchemaSpec by testSuite {
    modelTest(
        """
        |"Metadata": {
        |  "anyOf": [
        |    {
        |      "type": "object",
        |      "description": "Set of key-value pairs attached to an object.",
        |      "additionalProperties": {
        |        "type": "string"
        |      },
        |      "x-oaiTypeLabel": "map"
        |    },
        |    {
        |      "type": "null"
        |    }
        |  ]
        |}
        """.trimMargin(),
        "object/nullable-anyof-top-level-schema"
    )
}
