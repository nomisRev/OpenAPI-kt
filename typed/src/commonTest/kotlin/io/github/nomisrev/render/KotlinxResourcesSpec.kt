package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val kotlinxResourcesSpec by testSuite {
    verifyKotlin(
        name = "loads example.kt from kotlinTestData",
        resourceFile = "example.kt",
    ) {
        listOf(
            "fun main() {",
            "    println(\"Hello, World!\")",
            "}",
        ).joinToString(separator = "\n", postfix = "\n")
    }
}
