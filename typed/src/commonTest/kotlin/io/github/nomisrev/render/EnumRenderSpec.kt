package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext

val enumRenderSpec by testSuite {
    verify(
        """|@Serializable
           |enum class Sort {
           |    ASC, DESC;
           |}""".trimMargin(),
        Model.Enum(
            NamingContext.reference("Sort", SchemaContext.Null),
            Model.Primitive.String(null, null, null, false),
            listOf("ASC", "DESC"),
            null,
            false,
            null,
            null,
            false
        )
    )

    verify(
        """|@Serializable
           |enum class Sort {
           |    @SerialName("very_long_enum_value_1")
           |    VeryLongEnumValue1,
           |    @SerialName("very_long_enum_value_2")
           |    VeryLongEnumValue2,
           |    @SerialName("very_long_enum_value_3")
           |    VeryLongEnumValue3,
           |    @SerialName("very_long_enum_value_4")
           |    VeryLongEnumValue4,
           |    @SerialName("very_long_enum_value_5")
           |    VeryLongEnumValue5;
           |}""".trimMargin(),
        Model.Enum(
            NamingContext.reference("Sort", SchemaContext.Null),
            Model.Primitive.String(null, null, null, false),
            (1..5).map { "very_long_enum_value_$it" },
            null,
            false,
            null,
            null,
            false
        )
    )
}