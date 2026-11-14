package io.github.nomisrev.render

import io.github.nomisrev.openapi.toCaseName
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StatusCodeCaseNameTest {
    private val identifier = Regex("[A-Z][A-Za-z0-9]*")

    private val ktorKnown = HttpStatusCode.allStatusCodes.associateBy { it.value }

    @Test
    fun everyCodeHasAnIdentifierWithoutBackticks() {
        for (code in 100..599) {
            val name = HttpStatusCode.fromValue(code).toCaseName()
            assertTrue(identifier.matches(name), "$code -> '$name'")
        }
    }

    @Test
    fun namesAreUniquePerCode() {
        val names = (100..599).groupBy { HttpStatusCode.fromValue(it).toCaseName() }
        assertEquals(emptyMap(), names.filterValues { it.size > 1 })
    }

    @Test
    fun codesWithoutAnyNameAreStatusCode() {
        assertEquals("Status480", HttpStatusCode.fromValue(480).toCaseName())
        assertEquals("Status481", HttpStatusCode.fromValue(481).toCaseName())
        assertEquals("Status418", HttpStatusCode.fromValue(418).toCaseName())
        assertEquals("Status599", HttpStatusCode.fromValue(599).toCaseName())
    }

    @Test
    fun registeredCodesKtorLacksGetTheirIanaName() {
        val expected = mapOf(
            103 to "EarlyHints", 208 to "AlreadyReported", 226 to "ImUsed", 421 to "MisdirectedRequest",
            428 to "PreconditionRequired", 451 to "UnavailableForLegalReasons", 508 to "LoopDetected",
            510 to "NotExtended", 511 to "NetworkAuthenticationRequired",
        )
        for ([code, name] in expected) {
            assertEquals(name, HttpStatusCode.fromValue(code).toCaseName(), "$code")
        }
    }

    @Test
    fun hyphenatedKtorNamesAreFixedAndTheOthersUnchanged() {
        val renamed = mapOf(203 to "NonAuthoritativeInformation", 207 to "MultiStatus", 414 to "RequestUriTooLong")
        for ([code, status] in ktorKnown) {
            val old = status.description.split(" ").joinToString("") { w ->
                w.lowercase().replaceFirstChar { it.uppercase() }
            }
            assertEquals(renamed[code] ?: old, status.toCaseName(), "$code")
        }
    }
}
