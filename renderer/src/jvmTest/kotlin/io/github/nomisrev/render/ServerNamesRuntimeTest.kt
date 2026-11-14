package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.factory.server.names.Api
import io.github.nomisrev.render.test.client.factory.server.names.ApiServer
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `factory-server-names` golden: servers with one name are cases the caller can tell apart, and
 * the client sends its requests to the chosen server. The generated public constructors build their own client, whose
 * engine is the [RecordingEngineContainer] on the test classpath (the one of `ServerBasePathRuntimeTest`), so each
 * request URL is recorded and its scheme and host are asserted.
 */
class ServerNamesRuntimeTest {
    private val requests = RecordingEngineContainer.requests

    private suspend fun Api.pingedUrl(): Url {
        ping.get()
        return Url(requests.last())
    }

    @Test
    fun theDefaultIsTheFirstServerDeclaredWhicheverSchemeItHas() = runBlocking {
        // `https://api.example.com` is declared before `http://api.example.com`.
        Api().use { api ->
            val url = api.pingedUrl()
            assertEquals("https", url.protocol.name)
            assertEquals("api.example.com", url.host)
        }
        Api { }.use { api ->
            assertEquals("https://api.example.com/ping", api.pingedUrl().toString())
        }
    }

    @Test
    fun aNestedServerCanBeChosenByScheme() = runBlocking {
        Api(ApiServer.ApiExample.Http).use { api ->
            val url = api.pingedUrl()
            assertEquals("http", url.protocol.name)
            assertEquals("api.example.com", url.host)
        }
        Api(ApiServer.ApiExample.Https).use { api ->
            assertEquals("https", api.pingedUrl().protocol.name)
        }
    }

    @Test
    fun aNestedServerWithVariablesTakesItsVariables() = runBlocking {
        Api(ApiServer.RegionalEndpoint.Http()).use { api ->
            val url = api.pingedUrl()
            assertEquals("http", url.protocol.name)
            assertEquals("eu.regional.example.com", url.host)
        }
        Api(ApiServer.RegionalEndpoint.Https(ApiServer.RegionalEndpoint.Https.Region.Us)).use { api ->
            val url = api.pingedUrl()
            assertEquals("https", url.protocol.name)
            assertEquals("us.regional.example.com", url.host)
        }
    }

    @Test
    fun serversTheUrlWordsTellApartAndTheOthersKeepTheirUrls() {
        assertEquals("https://metrics.example.com/v1", ApiServer.MetricsV1.url)
        assertEquals("https://metrics.example.com/v2", ApiServer.MetricsV2.url)
        assertEquals("https://backup.example.com", ApiServer.Backup.url)
        assertEquals("http://api.example.com", ApiServer.ApiExample.Http.url)
        assertEquals("https://us.regional.example.com", ApiServer.RegionalEndpoint.Https(ApiServer.RegionalEndpoint.Https.Region.Us).url)
        assertEquals("http://x.example.com", ApiServer.Custom("http://x.example.com").url)
    }
}
