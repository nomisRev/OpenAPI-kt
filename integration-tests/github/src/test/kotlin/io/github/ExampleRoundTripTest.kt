package io.github

import io.github.nomisrev.openapi.roundtrip.RoundTripConfig
import io.github.nomisrev.openapi.roundtrip.runExampleRoundTrip
import io.ktor.serialization.kotlinx.json.DefaultJson
import kotlin.test.Test

class ExampleRoundTripTest {
    /** The generated client installs `ContentNegotiation { json() }`, i.e. Ktor's [DefaultJson]. */
    @Test
    fun `spec examples survive a decode and encode through the generated models`() =
        runExampleRoundTrip(RoundTripConfig.fromSystemProperties(DefaultJson))
}
