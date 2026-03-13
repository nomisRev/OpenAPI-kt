package io.github.nomisrev.render.golden.client.pascal_and_camel_case.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest

interface OpenAI {
    val fineTuning: FineTuning
}

internal class KtorOpenAI(private val client: HttpClient) : OpenAI {
    override val fineTuning: FineTuning = KtorFineTuning(client)
}

fun OpenAIClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {},
): OpenAI {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(baseUrl) }
        block()
    }
    return KtorOpenAI(client)
}
