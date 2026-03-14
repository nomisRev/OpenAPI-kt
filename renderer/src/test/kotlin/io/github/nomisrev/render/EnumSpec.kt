package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val primitiveSpec by testSuite {
    modelTest(
        """
        "Sort": {
          "type": "string",
          "description": "The sort order for results.",
          "enum": ["ASC", "DESC"]
        }""".trimMargin(),
        "enum/basic"
    )

    modelTest(
        """
        |"SortSerialName": {
        |  "type": "string",
        |  "enum": [
        |    "very_long_enum_value_1",
        |    "very_long_enum_value_2",
        |    "very_long_enum_value_3",
        |    "very_long_enum_value_4",
        |    "very_long_enum_value_5"
        |  ]
        |}""".trimMargin(),
        "enum/serialname"
    )

    modelTest(
        """
        |"Version": {
        |  "type": "string",
        |  "enum": ["v1"]
        |}
        """.trimMargin(),
        "enum/singlevalue"
    )

}