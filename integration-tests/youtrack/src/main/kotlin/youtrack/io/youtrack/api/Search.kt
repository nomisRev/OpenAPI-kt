package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.youtrack.model.SearchSuggestionsRead
import io.youtrack.model.SearchSuggestionsWrite
import kotlin.String

public class Search internal constructor(
  private val client: HttpClient,
) {
  public val assist: Assist = Assist(client)

  public class Assist internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,id,suggestions(${'$'}type,caret,completionEnd,completionStart,description,id,matchingEnd,matchingStart,option,prefix,suffix)", body: SearchSuggestionsWrite? = null): SearchSuggestionsRead = client.post("/search/assist") {
        fields?.let { parameter("fields", it) }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }
  }
}
