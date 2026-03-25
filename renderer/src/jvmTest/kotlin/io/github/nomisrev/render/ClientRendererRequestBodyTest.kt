package io.github.nomisrev.render

import io.github.nomisrev.openapi.countFlattenedBodyOverloadsOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClientRendererRequestBodyTest {
    @Test
    fun `flattened request-body overload count keeps the configured cap inclusive`() {
        assertEquals(32L, countFlattenedBodyOverloadsOrNull(List(5) { 2 }, maxOverloads = 32))
    }

    @Test
    fun `flattened request-body overload count stops before int overflow can bypass the cap`() {
        assertNull(countFlattenedBodyOverloadsOrNull(List(32) { 2 }, maxOverloads = 32))
    }
}
