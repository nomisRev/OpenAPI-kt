package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.root.`class`.name.path.Files
import io.github.nomisrev.render.test.client.root.`class`.name.path.FilesApi
import io.github.nomisrev.render.test.client.root.`class`.name.title.CSGOV3Scores
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `root-class-name-*` goldens: the client is created from its root class, whatever the title of
 * the document, and a root class that would have the name of a path class is a class of its own next to it.
 */
class RootClassNameRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val client = HttpClient(
        MockEngine { request ->
            requests += request
            respond("", HttpStatusCode.NoContent)
        }
    ) {
        defaultRequest { url("https://example.com") }
    }

    @Test
    fun aTitleWithAColonIsAClientClassNamedByItsWords() = runBlocking {
        CSGOV3Scores(client).scores.id("7").get()

        assertEquals("/scores/7", requests.last().url.encodedPath)
    }

    @Test
    fun aTitleWithTheNameOfAPathIsAClientClassNextToThePathClass() = runBlocking {
        val files: Files = FilesApi(client).files

        files.id("7").get()

        assertEquals("/files/7", requests.last().url.encodedPath)
    }
}
