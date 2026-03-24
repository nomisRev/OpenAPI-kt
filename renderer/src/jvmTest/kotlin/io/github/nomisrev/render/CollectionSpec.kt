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
        |"Payload": {
        |  "type": "array",
        |  "items": {},
        |  "default": []
        |}
        """.trimMargin(),
        "collection/freeform-default"
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

    modelTest(
        """
        |"CodeSecurityDefaultConfigurations": {
        |  "type": "array",
        |  "description": "A list of default code security configurations",
        |  "items": {
        |    "type": "object",
        |    "additionalProperties": false,
        |    "properties": {
        |      "default_for_new_repos": {
        |        "type": "string",
        |        "enum": ["public", "private_and_internal", "all"]
        |      },
        |      "name": { "type": "string" }
        |    }
        |  }
        |}
        """.trimMargin(),
        "collection/item-with-inline-enum"
    )

    modelTest(
        """
        |"SearchResultTextMatches": {
        |  "title": "Search Result Text Matches",
        |  "type": "array",
        |  "items": {
        |    "type": "object",
        |    "properties": {
        |      "object_url": { "type": "string" },
        |      "object_type": { "nullable": true, "type": "string" },
        |      "property": { "type": "string" },
        |      "fragment": { "type": "string" },
        |      "matches": {
        |        "type": "array",
        |        "items": {
        |          "type": "object",
        |          "properties": {
        |            "text": { "type": "string" },
        |            "indices": {
        |              "type": "array",
        |              "items": { "type": "integer" }
        |            }
        |          }
        |        }
        |      }
        |    }
        |  }
        |}
        """.trimMargin(),
        "collection/item-with-nested-object"
    )

    modelTest(
        """
        |"ModerationInputs": {
        |  "type": "array",
        |  "items": {
        |    "oneOf": [
        |      {
        |        "type": "object",
        |        "additionalProperties": false,
        |        "properties": {
        |          "type": { "type": "string", "enum": ["image_url"] },
        |          "image_url": {
        |            "type": "object",
        |            "additionalProperties": false,
        |            "properties": {
        |              "url": { "type": "string" }
        |            },
        |            "required": ["url"]
        |          }
        |        },
        |        "required": ["type", "image_url"]
        |      },
        |      {
        |        "type": "object",
        |        "additionalProperties": false,
        |        "properties": {
        |          "type": { "type": "string", "enum": ["text"] },
        |          "text": { "type": "string" }
        |        },
        |        "required": ["type", "text"]
        |      }
        |    ]
        |  }
        |}
        """.trimMargin(),
        "collection/item-with-nested-union"
    )
}
