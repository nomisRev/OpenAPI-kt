package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val discriminatedObjectSpec by testSuite {
    modelTest(
        $$"""
        |"User": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["anonymous"] },
        |    "id": { "type": "integer", "format": "int64" }
        |  },
        |  "required": ["type", "id"],
        |  "discriminator": {
        |    "propertyName": "type",
        |    "mapping": {
        |      "anonymous": "#/components/schemas/User",
        |      "registered": "#/components/schemas/RegisteredUser"
        |    }
        |  }
        |},
        |"RegisteredUser": {
        |  "allOf": [
        |    { "$ref": "#/components/schemas/User" },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "email": { "type": "string" }
        |      },
        |      "required": ["email"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "discriminated/basic"
    )

    modelTest(
        $$"""
        |"Account": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "kind": { "type": "string", "enum": ["local"] }
        |  },
        |  "required": ["kind"],
        |  "discriminator": {
        |    "propertyName": "kind",
        |    "mapping": {
        |      "local": "#/components/schemas/Account",
        |      "remote": "#/components/schemas/RemoteAccount"
        |    }
        |  }
        |},
        |"RemoteAccount": {
        |  "allOf": [
        |    { "$ref": "#/components/schemas/Account" },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "externalId": { "type": "string" }
        |      },
        |      "required": ["externalId"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "discriminated/value-class-subtype"
    )

    modelTest(
        $$"""
        |"Channel": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "kind": { "type": "string", "enum": ["sms"] }
        |  },
        |  "required": ["kind"],
        |  "discriminator": {
        |    "propertyName": "kind",
        |    "mapping": {
        |      "sms": "#/components/schemas/Channel",
        |      "email": "#/components/schemas/EmailChannel"
        |    }
        |  }
        |},
        |"EmailChannel": {
        |  "allOf": [
        |    { "$ref": "#/components/schemas/Channel" },
        |    {
        |      "type": "object",
        |      "additionalProperties": false
        |    }
        |  ]
        |}
        """.trimMargin(),
        "discriminated/data-object-subtype"
    )

    modelTest(
        $$"""
        |"Asset": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "kind": { "type": "string", "enum": ["image"] },
        |    "id": { "type": "string" },
        |    "createdAt": { "type": "string", "format": "date-time" }
        |  },
        |  "required": ["kind", "id", "createdAt"],
        |  "discriminator": {
        |    "propertyName": "kind",
        |    "mapping": {
        |      "image": "#/components/schemas/Asset",
        |      "video": "#/components/schemas/VideoAsset"
        |    }
        |  }
        |},
        |"VideoAsset": {
        |  "allOf": [
        |    { "$ref": "#/components/schemas/Asset" },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "durationSeconds": { "type": "integer", "format": "int32" }
        |      },
        |      "required": ["durationSeconds"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "discriminated/multiple-abstract"
    )

    modelTest(
        $$"""
        |"Event": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "kind": { "type": "string", "enum": ["user"] },
        |    "id": { "type": "integer", "format": "int64" }
        |  },
        |  "required": ["kind", "id"],
        |  "discriminator": {
        |    "propertyName": "kind",
        |    "mapping": {
        |      "user": "#/components/schemas/Event",
        |      "system": "#/components/schemas/SystemEvent"
        |    }
        |  }
        |},
        |"SystemEvent": {
        |  "allOf": [
        |    { "$ref": "#/components/schemas/Event" },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "payload": {
        |          "type": "object",
        |          "additionalProperties": false,
        |          "properties": {
        |            "source": { "type": "string" },
        |            "metadata": {
        |              "type": "object",
        |              "additionalProperties": false,
        |              "properties": {
        |                "attempt": { "type": "integer", "format": "int32" }
        |              },
        |              "required": ["attempt"]
        |            }
        |          },
        |          "required": ["source", "metadata"]
        |        },
        |        "items": {
        |          "type": "array",
        |          "items": {
        |            "type": "object",
        |            "additionalProperties": false,
        |            "properties": {
        |              "name": { "type": "string" },
        |              "enabled": { "type": "boolean" }
        |            },
        |            "required": ["name", "enabled"]
        |          }
        |        }
        |      },
        |      "required": ["payload", "items"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "discriminated/nested-properties"
    )
}
