package github.integration

import io.github.api.GitHubV3RESTAPI
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

suspend fun GitHubV3RESTAPI.example() {
    val issues = repos.owner("arrow-kt").repo("arrow").issues.get()
    println(issues)
}

fun main(): Unit = runBlocking {
    GitHubV3RESTAPI("https://api.github.com") {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }.example()
}
