package io.github.nomisrev.render.test.client.`impl`.full

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public interface Items {
  public suspend fun `get`(limit: Int? = null): List<String>

  public suspend fun post(body: String): PostResult

  public sealed interface PostResult {
    public data class Created(
      public val `value`: String,
    ) : PostResult

    public data class BadRequest(
      public val `value`: Int,
    ) : PostResult
  }
}

internal class KtorItems(
  private val client: HttpClient,
) : Items {
  override suspend fun `get`(limit: Int?): List<String> = client.get("/items") {
    limit?.let { parameter("limit", it) }
  }.body()

  override suspend fun post(body: String): Items.PostResult {
    val response = client.post("/items") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }
    return when (response.status.value) {
      201 -> Items.PostResult.Created(response.body())
      400 -> Items.PostResult.BadRequest(response.body())
      else -> throw ResponseException(response, "")
    }
  }
}
