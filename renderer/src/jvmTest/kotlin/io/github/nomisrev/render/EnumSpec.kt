package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val enumSpec by testSuite {
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

    modelTest(
        """
        |"Priority": {
        |  "type": "integer",
        |  "enum": ["1", "2", "3"]
        |}
        """.trimMargin(),
        "enum/int"
    )

    modelTest(
        """
        |"SpecialValues": {
        |  "type": "string",
        |  "enum": ["*", "/", "+1", "-1"]
        |}
        """.trimMargin(),
        "enum/special-chars"
    )

    modelTest(
        """
        |"NullableState": {
        |  "type": "string",
        |  "nullable": true,
        |  "enum": ["on", null, "off"]
        |}
        """.trimMargin(),
        "enum/nullable"
    )

    modelTest(
        """
        |"JsIdentifier": {
        |  "type": "string",
        |  "enum": ["1xx"]
        |}
        """.trimMargin(),
        "enum/jsname"
    )
}
