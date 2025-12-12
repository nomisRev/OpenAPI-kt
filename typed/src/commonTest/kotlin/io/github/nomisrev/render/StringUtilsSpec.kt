package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.render.splitToWords
import io.github.nomisrev.openapi.render.toCamelCase
import io.github.nomisrev.openapi.render.toPascalCase
import kotlin.test.assertEquals

val stringUtilsSpec by testSuite {
    test("VERY_LONG_ENUM_VALUE_1.toPascalCase() == VERYLONGENUMVALUE1") {
        assertEquals("VERYLONGENUMVALUE1", "VERY_LONG_ENUM_VALUE_1".toPascalCase())
    }

    test("very_long_enum_value_1.toPascalCase() == VERYLONGENUMVALUE1") {
        assertEquals("VeryLongEnumValue1", "very_long_enum_value_1".toPascalCase())
    }

    listOf(
        "very_long_enum_value_1" to listOf("very", "long", "enum", "value", "1"),
        "very_long enum_value_1" to listOf("very", "long", "enum", "value", "1"),
        "very_long enum.value-1" to listOf("very", "long", "enum", "value", "1"),
        "very/long enum.value-1" to listOf("very", "long", "enum", "value", "1"),
        "very/long[enum.value-1" to listOf("very", "long", "enum", "value", "1"),
        "very/long[enum]value-1" to listOf("very", "long", "enum", "value", "1"),
        "very/long*enum]value-1" to listOf("very", "long", "enum", "value", "1"),
    ).forEach { (original, expected) ->
        test("$original.splitToWords() == $expected") {
            assertEquals(expected, original.splitToWords())
        }
    }
}