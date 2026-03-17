package io.github.nomisrev.render.test.client.`inline`.oneof.request.body.nested.path

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json.Default.decodeFromJsonElement
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public interface User {
  public val codespaces: Codespaces

  public interface Codespaces {
    public val post: Post

    public interface Post {
      public suspend operator fun invoke(body: Body): Response

      @Serializable(with = Body.Serializer::class)
      public sealed interface Body {
        @Serializable
        public data class RepositoryIdAndRef(
          @SerialName("repository_id")
          public val repositoryId: Long,
          public val ref: String? = null,
        ) : Body

        @JvmInline
        @Serializable
        public value class PullRequest(
          @SerialName("pull_request")
          public val pullRequest: PullRequest,
        ) : Body {
          @Serializable
          public data class PullRequest(
            @SerialName("pull_request_number")
            public val pullRequestNumber: Long,
            @SerialName("repository_id")
            public val repositoryId: Long,
          )
        }

        public object Serializer : KSerializer<Body> {
          @OptIn(
            InternalSerializationApi::class,
            ExperimentalSerializationApi::class,
          )
          override val descriptor: SerialDescriptor =
              buildSerialDescriptor("io.github.nomisrev.render.test.client.inline.oneof.request.body.nested.path.User.Codespaces.Post.Body", PolymorphicKind.SEALED) {
            element("RepositoryIdAndRef", RepositoryIdAndRef.serializer().descriptor)
            element("PullRequest", PullRequest.serializer().descriptor)
          }

          override fun deserialize(decoder: Decoder): Body {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
              value,
              RepositoryIdAndRef::class to { decodeFromJsonElement(RepositoryIdAndRef.serializer(), it) },
              PullRequest::class to { decodeFromJsonElement(PullRequest.serializer(), it) },
            )
          }

          override fun serialize(encoder: Encoder, `value`: Body) {
            when(value) {
              is RepositoryIdAndRef -> encoder.encodeSerializableValue(RepositoryIdAndRef.serializer(), value)
              is PullRequest -> encoder.encodeSerializableValue(PullRequest.serializer(), value)
            }
          }
        }
      }

      public data class Response(
        public val `value`: String,
      )
    }
  }
}

internal class KtorUser(
  private val client: HttpClient,
) : User {
  override val codespaces: User.Codespaces = KtorUserCodespaces(client)
}

internal class KtorUserCodespaces(
  private val client: HttpClient,
) : User.Codespaces {
  override val post: User.Codespaces.Post = object : User.Codespaces.Post {
    override suspend operator fun invoke(body: User.Codespaces.Post.Body): User.Codespaces.Post.Response {
      val value: String = client.post("/user/codespaces") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
      return User.Codespaces.Post.Response(value)
    }
  }
}
