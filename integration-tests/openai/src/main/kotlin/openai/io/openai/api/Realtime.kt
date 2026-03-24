package io.openai.api

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
import io.openai.model.RealtimeCallCreateRequest
import io.openai.model.RealtimeCallReferRequest
import io.openai.model.RealtimeCallRejectRequest
import io.openai.model.RealtimeCreateClientSecretRequest
import io.openai.model.RealtimeCreateClientSecretResponse
import io.openai.model.RealtimeSessionCreateRequest
import io.openai.model.RealtimeSessionCreateRequestGA
import io.openai.model.RealtimeSessionCreateResponse
import io.openai.model.RealtimeTranscriptionSessionCreateRequest
import io.openai.model.RealtimeTranscriptionSessionCreateResponse
import kotlin.String
import kotlinx.serialization.json.Json

public class Realtime internal constructor(
  private val client: HttpClient,
) {
  public val calls: Calls = Calls(client)

  public val clientSecrets: ClientSecrets = ClientSecrets(client)

  public val sessions: Sessions = Sessions(client)

  public val transcriptionSessions: TranscriptionSessions = TranscriptionSessions(client)

  public class Calls internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public fun callId(callId: String): CallIdPath = CallIdPath(client, callId)

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
    }

    public class CallIdPath internal constructor(
      private val client: HttpClient,
      private val callId: String,
    ) {
      public val accept: Accept = Accept(client, callId)

      public val hangup: Hangup = Hangup(client, callId)

      public val refer: Refer = Refer(client, callId)

      public val reject: Reject = Reject(client, callId)

      public class Accept internal constructor(
        private val client: HttpClient,
        private val callId: String,
      ) {
        public val post: Post = Post(client, callId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val callId: String,
        ) {
          public suspend operator fun invoke(body: RealtimeSessionCreateRequestGA) {
            client.post("/realtime/calls/$callId/accept") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
          }
        }
      }

      public class Hangup internal constructor(
        private val client: HttpClient,
        private val callId: String,
      ) {
        public val post: Post = Post(client, callId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val callId: String,
        ) {
          public suspend operator fun invoke() {
            client.post("/realtime/calls/$callId/hangup")
          }
        }
      }

      public class Refer internal constructor(
        private val client: HttpClient,
        private val callId: String,
      ) {
        public val post: Post = Post(client, callId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val callId: String,
        ) {
          public suspend operator fun invoke(body: RealtimeCallReferRequest) {
            client.post("/realtime/calls/$callId/refer") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
          }
        }
      }

      public class Reject internal constructor(
        private val client: HttpClient,
        private val callId: String,
      ) {
        public val post: Post = Post(client, callId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val callId: String,
        ) {
          public suspend operator fun invoke(body: RealtimeCallRejectRequest? = null) {
            client.post("/realtime/calls/$callId/reject") {
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
          }
        }
      }
    }
  }

  public class ClientSecrets internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: RealtimeCreateClientSecretRequest): RealtimeCreateClientSecretResponse = client.post("/realtime/client_secrets") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }
  }

  public class Sessions internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: RealtimeSessionCreateRequest): RealtimeSessionCreateResponse = client.post("/realtime/sessions") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }
  }

  public class TranscriptionSessions internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: RealtimeTranscriptionSessionCreateRequest): RealtimeTranscriptionSessionCreateResponse = client.post("/realtime/transcription_sessions") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }
  }
}
