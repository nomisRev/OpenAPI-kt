package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.youtrack.model.IssueCountResponseRead
import io.youtrack.model.IssueCountResponseWrite
import kotlin.String

public class IssuesGetter internal constructor(
  private val client: HttpClient,
) {
  public val count: Count = Count(client)

  public class Count internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,count,id", body: IssueCountResponseWrite? = null): IssueCountResponseRead = client.post("/issuesGetter/count") {
        fields?.let { parameter("fields", it) }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }
  }
}
