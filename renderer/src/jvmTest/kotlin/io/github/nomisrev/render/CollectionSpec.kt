package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val collectionSpec by testSuite {
    modelTest(
        """
        |"Tags": {
        |  "type": "array",
        |  "items": { "type": "string" }
        |}
        """.trimMargin(),
        "collection/basic"
    )

    modelTest(
        """
        |"Pets": {
        |  "type": "array",
        |  "items": {
        |    "type": "object",
        |    "additionalProperties": false,
        |    "properties": {
        |      "id": { "type": "string" },
        |      "name": { "type": "string" }
        |    },
        |    "required": ["id", "name"]
        |  }
        |}
        """.trimMargin(),
        "collection/complex-inner"
    )

    modelTest(
        """
        |"Payload": {
        |  "type": "array",
        |  "items": {}
        |}
        """.trimMargin(),
        "collection/freeform"
    )

    modelTest(
        """
        |"MaybeTags": {
        |  "type": "array",
        |  "nullable": true,
        |  "items": { "type": "string" }
        |}
        """.trimMargin(),
        "collection/nullable"
    )
}
