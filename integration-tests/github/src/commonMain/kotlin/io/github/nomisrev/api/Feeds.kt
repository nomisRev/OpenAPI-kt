package io.github.nomisrev.api

import io.github.nomisrev.model.Feed
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface Feeds {
    suspend fun activityGetFeeds(): Feed
}

internal class KtorFeeds(private val client: HttpClient) : Feeds {
    override suspend fun activityGetFeeds(): Feed =
        client.get("/feeds").body()
}
