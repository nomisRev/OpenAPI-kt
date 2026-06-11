package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.youtrack.model.CommandListRead
import io.youtrack.model.CommandListWrite
import kotlin.Boolean
import kotlin.String

public class Commands internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val assist: Assist = Assist(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      muteUpdateNotifications: Boolean? = null,
      fields: String? = "${'$'}type,caret,commands(${'$'}type,description,error,id),comment,id,issues(${'$'}type,id,idReadable,numberInProject),query,suggestions(${'$'}type,caret,completionEnd,completionStart,description,id,matchingEnd,matchingStart,option,prefix,suffix)",
      body: CommandListWrite? = null,
    ): CommandListRead = client.post("/commands") {
      muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
      fields?.let { parameter("fields", it) }
      body?.let {
        contentType(ContentType.Application.Json)
        setBody(it)
      }
    }.body()
  }

  public class Assist internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,caret,commands(${'$'}type,description,error,id),comment,id,issues(${'$'}type,id,idReadable,numberInProject),query,suggestions(${'$'}type,caret,completionEnd,completionStart,description,id,matchingEnd,matchingStart,option,prefix,suffix)", body: CommandListWrite? = null): CommandListRead = client.post("/commands/assist") {
        fields?.let { parameter("fields", it) }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }
  }
}
