package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.requireUnique
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

val utilSpec by testSuite {
    test("emptylist passes unique check") {
        requireUnique(emptyList<Int>())
    }

    test("single item list passes unique check") {
        requireUnique(listOf(1))
    }

    test("unique list passes unique check") {
        requireUnique(listOf(1, 2, 3, 4))
    }

    test("unique list passes unique check") {
        val ex = assertFailsWith<IllegalArgumentException> {
            requireUnique(listOf(1, 2, 1, 4, 3, 2))
        }
        assertEquals("Non-unique entries found: [1, 2]", ex.message)
    }

    test("custom error message") {
        val ex = assertFailsWith<IllegalArgumentException> {
            requireUnique(listOf(1, 2, 1, 4, 3, 2), "message")
        }
        assertEquals("message", ex.message)
    }
}
