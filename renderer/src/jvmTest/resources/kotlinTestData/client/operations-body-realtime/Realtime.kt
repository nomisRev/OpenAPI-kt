package io.github.nomisrev.render.test.client.operations.body.realtime

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlin.String
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

public class Realtime internal constructor(
  private val client: HttpClient,
) {
  public val calls: Calls = Calls(client)

  public class Calls internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(sdp: String, session: RealtimeCallCreateRequest.Session? = null): String = client.post("/realtime/calls") {
        setBody(MultiPartFormDataContent(formData {
          append("sdp", sdp, headersOf(HttpHeaders.ContentType, "application/sdp"))
          if (session != null) {
            append("session", Json.encodeToString(RealtimeCallCreateRequest.Session.serializer(), session), headersOf(HttpHeaders.ContentType, "application/json"))
          }
        }))
      }.body()

      public suspend operator fun invoke(body: String): String = client.post("/realtime/calls") {
        contentType(ContentType("application", "sdp"))
        setBody(body)
      }.body()

      public object Body {
        @Serializable
        public data class Session(
          public val type: String,
          public val model: String,
        )
      }
    }
  }
}
