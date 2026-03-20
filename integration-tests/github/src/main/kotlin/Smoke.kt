package github.integration

import io.github.api.GitHubV3RESTAPI
import io.github.api.Repos
import io.github.api.Repos.OwnerPath.RepoPath.Issues
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking

suspend fun GitHubV3RESTAPI.example() {
    val issues = repos.owner("ktorio").repo("ktor").issues.get()
    repos.owner("ktorio").repo("ktor").issues.post(
        Issues.Post.Body(
            title = Issues.Post.Body.Title.CaseString("My new title!")
        )
    )

    val titles = when (issues) {
        is Repos.OwnerPath.RepoPath.Issues.Get.Response.Ok -> issues.value.map { it.title }
        is Repos.OwnerPath.RepoPath.Issues.Get.Response.MovedPermanently,
        is Repos.OwnerPath.RepoPath.Issues.Get.Response.NotFound,
        is Repos.OwnerPath.RepoPath.Issues.Get.Response.UnprocessableEntity -> null
    }
    println(titles.orEmpty().joinToString("\n") { " - $it" })
}

fun main(): Unit = runBlocking {
    GitHubV3RESTAPI("https://api.github.com") {
        install(ContentNegotiation) { json() }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }.example()
}
