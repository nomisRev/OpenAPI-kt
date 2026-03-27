package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val unionSpec by testSuite {
    modelTest(
        """
        |"DiscriminatedBasicUnion": {
        |  "oneOf": [
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "type": { "type": "string", "enum": ["employee"] },
        |        "name": { "type": "string" }
        |      },
        |      "required": ["type", "name"]
        |    },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "type": { "type": "string", "enum": ["manager"] },
        |        "level": { "type": "integer", "format": "int32" }
        |      },
        |      "required": ["type", "level"]
        |    }
        |  ],
        |  "discriminator": { "propertyName": "type" }
        |}
        """.trimMargin(),
        "union/discriminated-basic"
    )

    modelTest(
        $$"""
        |"DiscriminatedWrappedUnion": {
        |  "oneOf": [
        |    { "$ref": "#/components/schemas/WrappedEmployee" },
        |    { "$ref": "#/components/schemas/WrappedContractor" }
        |  ],
        |  "discriminator": {
        |    "propertyName": "kind",
        |    "mapping": {
        |      "employee": "#/components/schemas/WrappedEmployee",
        |      "contractor": "#/components/schemas/WrappedContractor"
        |    }
        |  }
        |},
        |"WrappedEmployee": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "name": { "type": "string" }
        |  },
        |  "required": ["name"]
        |},
        |"WrappedContractor": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "name": { "type": "string" }
        |  },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "union/discriminated-wrapped"
    )

    modelTest(
        """
        |"DiscriminatedInlinedUnion": {
        |  "oneOf": [
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "event": { "type": "string", "enum": ["created"] },
        |        "id": { "type": "string" }
        |      },
        |      "required": ["event", "id"]
        |    },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "event": { "type": "string", "enum": ["deleted"] },
        |        "hard": { "type": "boolean" }
        |      },
        |      "required": ["event", "hard"]
        |    }
        |  ],
        |  "discriminator": { "propertyName": "event" }
        |}
        """.trimMargin(),
        "union/discriminated-inlined"
    )

    modelTest(
        $$"""
        |"DiscriminatedMixedUnion": {
        |  "oneOf": [
        |    { "$ref": "#/components/schemas/MixedUser" },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "kind": { "type": "string", "enum": ["guest"] },
        |        "id": { "type": "integer", "format": "int32" }
        |      },
        |      "required": ["kind", "id"]
        |    }
        |  ],
        |  "discriminator": {
        |    "propertyName": "kind",
        |    "mapping": {
        |      "user": "#/components/schemas/MixedUser"
        |    }
        |  }
        |},
        |"MixedUser": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "name": { "type": "string" }
        |  },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "union/discriminated-mixed"
    )

    modelTest(
        $$"""
        |"ChatCompletionRequestUserMessageContentPart": {
        |  "oneOf": [
        |    { "$ref": "#/components/schemas/ChatCompletionRequestMessageContentPartText" },
        |    { "$ref": "#/components/schemas/ChatCompletionRequestMessageContentPartImage" }
        |  ]
        |},
        |"ChatCompletionRequestMessageContentPartText": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["text"] },
        |    "text": { "type": "string" }
        |  },
        |  "required": ["type", "text"]
        |},
        |"ChatCompletionRequestMessageContentPartImage": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["image_url"] },
        |    "image_url": {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "url": { "type": "string" }
        |      },
        |      "required": ["url"]
        |    }
        |  },
        |  "required": ["type", "image_url"]
        |}
        """.trimMargin(),
        "union/discriminated-inferred-openai-content-parts"
    )

    modelTest(
        $$"""
        |"DiscriminatedEnumUnion": {
        |  "oneOf": [
        |    {
        |      "type": "string",
        |      "enum": ["asc", "desc"]
        |    },
        |    { "$ref": "#/components/schemas/EnumManual" }
        |  ],
        |  "discriminator": {
        |    "propertyName": "kind",
        |    "mapping": {
        |      "manual": "#/components/schemas/EnumManual"
        |    }
        |  }
        |},
        |"EnumManual": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "kind": { "type": "string" }
        |  },
        |  "required": ["kind"]
        |}
        """.trimMargin(),
        "union/discriminated-enum-case"
    )

    modelTest(
        $$"""
        |"DiscriminatedPrimitiveUnion": {
        |  "oneOf": [
        |    { "type": "string" },
        |    { "$ref": "#/components/schemas/PrimitiveEmployee" }
        |  ]
        |},
        |"PrimitiveEmployee": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "kind": { "type": "string" },
        |    "age": { "type": "integer", "format": "int32" }
        |  },
        |  "required": ["kind", "age"]
        |}
        """.trimMargin(),
        "union/discriminated-primitive"
    )

    modelTest(
        """
        |"DiscriminatedValueClassUnion": {
        |  "oneOf": [
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "kind": { "type": "string", "enum": ["inline"] }
        |      },
        |      "required": ["kind"]
        |    },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "kind": { "type": "string", "enum": ["remote"] },
        |        "id": { "type": "string" }
        |      },
        |      "required": ["kind", "id"]
        |    }
        |  ],
        |  "discriminator": {
        |    "propertyName": "kind"
        |  }
        |}
        """.trimMargin(),
        "union/discriminated-value-class-case"
    )

    modelTest(
        $$"""
        |"CreateSpeechResponseStreamEvent": {
        |  "anyOf": [
        |    { "$ref": "#/components/schemas/SpeechAudioDeltaEvent" },
        |    { "$ref": "#/components/schemas/SpeechAudioDoneEvent" }
        |  ],
        |  "discriminator": { "propertyName": "type" }
        |},
        |"SpeechAudioDeltaEvent": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["speech.audio.delta"] },
        |    "audio": { "type": "string" }
        |  },
        |  "required": ["type", "audio"]
        |},
        |"SpeechAudioDoneEvent": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["speech.audio.done"] },
        |    "usage": {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "input_tokens": { "type": "integer" },
        |        "output_tokens": { "type": "integer" },
        |        "total_tokens": { "type": "integer" }
        |      },
        |      "required": ["input_tokens", "output_tokens", "total_tokens"]
        |    }
        |  },
        |  "required": ["type", "usage"]
        |}
        """.trimMargin(),
        "union/discriminated-anyof-openai-speech"
    )

    modelTest(
        $$"""
        |"CompoundFilter": {
        |  "$recursiveAnchor": true,
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["and", "or"] },
        |    "filters": {
        |      "type": "array",
        |      "items": {
        |        "oneOf": [
        |          { "$ref": "#/components/schemas/ComparisonFilter" },
        |          { "$recursiveRef": "#" }
        |        ],
        |        "discriminator": { "propertyName": "type" }
        |      }
        |    }
        |  },
        |  "required": ["type", "filters"]
        |},
        |"ComparisonFilter": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["eq", "ne"] },
        |    "key": { "type": "string" },
        |    "value": { "type": "string" }
        |  },
        |  "required": ["type", "key", "value"]
        |}
        """.trimMargin(),
        "union/discriminated-recursive-ref-fallback"
    )

    modelTest(
        $$"""
        |"ConversationItem": {
        |  "anyOf": [
        |    { "$ref": "#/components/schemas/MessageItem" },
        |    { "$ref": "#/components/schemas/FunctionToolCallResource" }
        |  ],
        |  "discriminator": { "propertyName": "type" }
        |},
        |"MessageItem": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["message"] },
        |    "text": { "type": "string" }
        |  },
        |  "required": ["type", "text"]
        |},
        |"FunctionToolCall": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "type": { "type": "string", "enum": ["function_call"] },
        |    "call_id": { "type": "string" }
        |  },
        |  "required": ["type", "call_id"]
        |},
        |"FunctionToolCallResource": {
        |  "allOf": [
        |    { "$ref": "#/components/schemas/FunctionToolCall" },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "id": { "type": "string" }
        |      },
        |      "required": ["id"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "union/discriminated-anyof-inherited-allof-tag"
    )

    // ── Phase 6: Non-discriminated unions ───────────────────────────────

    // Union of all primitive types
    modelTest(
        """
        |"Union": {
        |  "oneOf": [
        |    { "type": "string" },
        |    { "type": "integer", "format": "int32" },
        |    { "type": "number", "format": "float" },
        |    { "type": "number", "format": "double" },
        |    { "type": "boolean" },
        |    { "type": "string", "format": "date" },
        |    { "type": "string", "format": "date-time" },
        |    { "type": "string", "format": "binary" },
        |    { "type": "string", "format": "uuid" }
        |  ]
        |}
        """.trimMargin(),
        "union/all-primitives"
    )

    // Open enum pattern: Enum + String
    modelTest(
        """
        |"Union": {
        |  "oneOf": [
        |    { "type": "string", "enum": ["asc", "desc"] },
        |    { "type": "string" }
        |  ]
        |}
        """.trimMargin(),
        "union/enum-and-primitive"
    )

    // Collection + primitive
    modelTest(
        """
        |"Union": {
        |  "oneOf": [
        |    { "type": "array", "items": { "type": "string" } },
        |    { "type": "integer", "format": "int32" }
        |  ]
        |}
        """.trimMargin(),
        "union/collection-and-primitive"
    )

    modelTest(
        """
        |"CreateModerationRequest": {
        |  "type": "object",
        |  "properties": {
        |    "input": {
        |      "oneOf": [
        |        { "type": "string" },
        |        {
        |          "type": "array",
        |          "items": { "type": "string" }
        |        },
        |        {
        |          "type": "array",
        |          "items": {
        |            "oneOf": [
        |              {
        |                "type": "object",
        |                "additionalProperties": false,
        |                "properties": {
        |                  "type": { "type": "string", "enum": ["image_url"] },
        |                  "image_url": {
        |                    "type": "object",
        |                    "additionalProperties": false,
        |                    "properties": {
        |                      "url": { "type": "string" }
        |                    },
        |                    "required": ["url"]
        |                  }
        |                },
        |                "required": ["type", "image_url"]
        |              },
        |              {
        |                "type": "object",
        |                "additionalProperties": false,
        |                "properties": {
        |                  "type": { "type": "string", "enum": ["text"] },
        |                  "text": { "type": "string" }
        |                },
        |                "required": ["type", "text"]
        |              }
        |            ]
        |          }
        |        }
        |      ]
        |    }
        |  },
        |  "required": ["input"]
        |}
        """.trimMargin(),
        "union/collection-item-union"
    )

    modelTest(
        """
        |"Union": {
        |  "oneOf": [
        |    { "type": "string", "nullable": true },
        |    { "type": "integer", "format": "int32" },
        |    { "type": "array", "nullable": true, "items": { "type": "string" } }
        |  ]
        |}
        """.trimMargin(),
        "union/nullable-cases"
    )

    // Inlined object + primitives (deserialization order: object first, string last)
    modelTest(
        """
        |"Union": {
        |  "oneOf": [
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "id": { "type": "integer", "format": "int32" }
        |      },
        |      "required": ["id"]
        |    },
        |    { "type": "string" },
        |    { "type": "boolean" }
        |  ]
        |}
        """.trimMargin(),
        "union/inlined-object-and-primitives"
    )

    // References
    modelTest(
        $$"""
        |"Union": {
        |  "oneOf": [
        |    { "$ref": "#/components/schemas/Person" },
        |    { "$ref": "#/components/schemas/Company" }
        |  ]
        |},
        |"Person": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "name": { "type": "string" },
        |    "age": { "type": "integer", "format": "int32" }
        |  },
        |  "required": ["name", "age"]
        |},
        |"Company": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "name": { "type": "string" },
        |    "employeeCount": { "type": "integer", "format": "int32" }
        |  },
        |  "required": ["name", "employeeCount"]
        |}
        """.trimMargin(),
        "union/references"
    )

    modelTest(
        """
        |"Union": {
        |  "oneOf": [
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "a": { "type": "string" },
        |        "b": { "type": "string" }
        |      },
        |      "required": ["a", "b"]
        |    },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "a": { "type": "string" },
        |        "b": { "type": "string" },
        |        "c": { "type": "string" }
        |      },
        |      "required": ["a", "b", "c"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "union/overlapping-objects"
    )

    modelTest(
        """
        |"ProtectionRules": {
        |  "anyOf": [
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "id": { "type": "integer", "format": "int32" },
        |        "node_id": { "type": "string" },
        |        "type": { "type": "string", "example": "wait_timer" },
        |        "wait_timer": { "type": "integer", "format": "int32" }
        |      },
        |      "required": ["id", "node_id", "type"]
        |    },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "id": { "type": "integer", "format": "int32" },
        |        "node_id": { "type": "string" },
        |        "type": { "type": "string", "example": "required_reviewers" },
        |        "prevent_self_review": { "type": "boolean" },
        |        "reviewers": {
        |          "type": "array",
        |          "items": { "type": "string" }
        |        }
        |      },
        |      "required": ["id", "node_id", "type"]
        |    },
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "id": { "type": "integer", "format": "int32" },
        |        "node_id": { "type": "string" },
        |        "type": { "type": "string", "example": "branch_policy" }
        |      },
        |      "required": ["id", "node_id", "type"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "union/protection-rules"
    )

    modelTest(
        """
        |"Union": {
        |  "oneOf": [
        |    {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "id": { "type": "integer", "format": "int32" },
        |        "name": { "type": "string" }
        |      },
        |      "required": ["id", "name"]
        |    },
        |    {
        |      "type": "object",
        |      "additionalProperties": true,
        |      "properties": {
        |        "name": { "type": "string" }
        |      },
        |      "required": ["name"]
        |    }
        |  ]
        |}
        """.trimMargin(),
        "union/additional-properties-last"
    )
}
