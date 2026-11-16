package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.http.headersOf
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
  private val __client: HttpClient,
) {
  public val calls: Calls = Calls(__client)

  public val clientSecrets: ClientSecrets = ClientSecrets(__client)

  public val sessions: Sessions = Sessions(__client)

  public val transcriptionSessions: TranscriptionSessions = TranscriptionSessions(__client)

  public class Calls internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public fun callId(callId: String): CallIdPath = CallIdPath(__client, callId)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(sdp: String, session: RealtimeSessionCreateRequestGA? = null): String {
        val response = __client.post("realtime/calls") {
          setBody(MultiPartFormDataContent(formData {
            append("sdp", sdp, headersOf(HttpHeaders.ContentType, "application/sdp"))
            if (session != null) {
              append("session", Json.encodeToString(RealtimeSessionCreateRequestGA.serializer(), session), headersOf(HttpHeaders.ContentType, "application/json"))
            }
          }))
        }
        if (response.status.value != 201) throw ResponseException(response, "")
        return response.body()
      }

      public suspend operator fun invoke(body: String): String {
        val response = __client.post("realtime/calls") {
          contentType(ContentType("application", "sdp"))
          setBody(body)
        }
        if (response.status.value != 201) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class CallIdPath internal constructor(
      private val __client: HttpClient,
      private val _callId: String,
    ) {
      public val accept: Accept = Accept(__client, _callId)

      public val hangup: Hangup = Hangup(__client, _callId)

      public val refer: Refer = Refer(__client, _callId)

      public val reject: Reject = Reject(__client, _callId)

      public class Accept internal constructor(
        private val __client: HttpClient,
        private val _callId: String,
      ) {
        public val post: Post = Post(__client, _callId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _callId: String,
        ) {
          public suspend operator fun invoke(body: RealtimeSessionCreateRequestGA) {
            val response = __client.post("realtime/calls/${this._callId.encodeURLParameter()}/accept") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }
      }

      public class Hangup internal constructor(
        private val __client: HttpClient,
        private val _callId: String,
      ) {
        public val post: Post = Post(__client, _callId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _callId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.post("realtime/calls/${this._callId.encodeURLParameter()}/hangup")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }
      }

      public class Refer internal constructor(
        private val __client: HttpClient,
        private val _callId: String,
      ) {
        public val post: Post = Post(__client, _callId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _callId: String,
        ) {
          public suspend operator fun invoke(body: RealtimeCallReferRequest) {
            val response = __client.post("realtime/calls/${this._callId.encodeURLParameter()}/refer") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }
      }

      public class Reject internal constructor(
        private val __client: HttpClient,
        private val _callId: String,
      ) {
        public val post: Post = Post(__client, _callId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _callId: String,
        ) {
          public suspend operator fun invoke(body: RealtimeCallRejectRequest? = null) {
            val response = __client.post("realtime/calls/${this._callId.encodeURLParameter()}/reject") {
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }
      }
    }
  }

  public class ClientSecrets internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: RealtimeCreateClientSecretRequest): RealtimeCreateClientSecretResponse {
        val response = __client.post("realtime/client_secrets") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Sessions internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: RealtimeSessionCreateRequest): RealtimeSessionCreateResponse {
        val response = __client.post("realtime/sessions") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class TranscriptionSessions internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: RealtimeTranscriptionSessionCreateRequest): RealtimeTranscriptionSessionCreateResponse {
        val response = __client.post("realtime/transcription_sessions") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
