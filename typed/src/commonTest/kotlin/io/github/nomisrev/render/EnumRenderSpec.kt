package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmInline
import kotlin.test.assertEquals

val enumRenderSpec by testSuite {
    verify(
        """|@Serializable
           |enum class Sort {
           |    ASC, DESC;
           |}""".trimMargin(),
        Model.Enum(
            NamingContext.reference("Sort", SchemaContext.Null),
            Model.Primitive.String(null, null, null, false, null),
            listOf("ASC", "DESC"),
            null,
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
            Model.Primitive.String(null, null, null, false, null),
            (1..5).map { "very_long_enum_value_$it" },
            null,
            null,
            null,
            false
        )
    )

    test("asc") {
        val actual = Json.decodeFromString(Test.Serializer, "\"asc\"")
        assertEquals(AscOrDesc.Asc, actual)
    }

    test("desc") {
        val actual = Json.decodeFromString(Test.Serializer, "\"desc\"")
        assertEquals(AscOrDesc.Desc, actual)
    }

    test("open") {
        val actual = Json.decodeFromString(Test.Serializer, "\"any\"")
        assertEquals(CaseString("any"), actual)
    }
}

// TODO Example of OpenEnum generation Serializer
@Serializable(with = Test.Serializer::class)
sealed interface Test {
    companion object Serializer : KSerializer<Test> {
        @OptIn(InternalSerializationApi::class)
        override val descriptor: SerialDescriptor = buildSerialDescriptor("Test", SerialKind.CONTEXTUAL)

        override fun serialize(encoder: Encoder, value: Test) =
            when (value) {
                AscOrDesc.Asc -> encoder.encodeString("asc")
                AscOrDesc.Desc -> encoder.encodeString("desc")
                is CaseString -> encoder.encodeString(value.value)
            }

        override fun deserialize(decoder: Decoder): Test =
            when (val value = decoder.decodeString()) {
                "asc" -> AscOrDesc.Asc
                "desc" -> AscOrDesc.Desc
                else -> CaseString(value)
            }
    }
}

@Serializable
@JvmInline
value class CaseString(val value: String) : Test

@Serializable
enum class AscOrDesc : Test { Asc, Desc }
