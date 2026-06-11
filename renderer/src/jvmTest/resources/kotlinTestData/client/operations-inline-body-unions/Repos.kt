package io.github.nomisrev.render.test.client.operations.`inline`.body.unions

import io.ktor.client.HttpClient
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public class Repos internal constructor(
  private val client: HttpClient,
) {
  public fun owner(owner: String): OwnerPath = OwnerPath(client, owner)

  public class OwnerPath internal constructor(
    private val client: HttpClient,
    private val owner: String,
  ) {
    public fun repo(repo: String): RepoPath = RepoPath(client, owner, repo)

    public class RepoPath internal constructor(
      private val client: HttpClient,
      private val owner: String,
      private val repo: String,
    ) {
      public val issues: Issues = Issues(client, owner, repo)

      public class Issues internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            title: String,
            body: String? = null,
            milestone: Milestone? = null,
          ) {
            client.post("/repos/$owner/$repo/issues") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = Body.Title.CaseString(title), body = body, milestone = milestone))
            }
          }

          public suspend operator fun invoke(
            title: Long,
            body: String? = null,
            milestone: Milestone? = null,
          ) {
            client.post("/repos/$owner/$repo/issues") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = Body.Title.CaseLong(title), body = body, milestone = milestone))
            }
          }

          @Serializable(with = Milestone.Serializer::class)
          public sealed interface Milestone {
            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : Milestone

            @Serializable
            @JvmInline
            public value class CaseLong(
              public val `value`: Long,
            ) : Milestone

            public object Serializer : KSerializer<Milestone> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.nomisrev.render.test.client.operations.inline.body.unions.Repos.OwnerPath.RepoPath.Issues.Post.Milestone", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("CaseLong", Long.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Milestone {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                  value,
                  CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                  CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
              }

              override fun serialize(encoder: Encoder, `value`: Milestone) {
                when(value) {
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                  is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                }
              }
            }
          }

          @Serializable
          internal data class Body(
            public val title: Title,
            public val body: String? = null,
            public val milestone: Milestone? = null,
          ) {
            @Serializable(with = Title.Serializer::class)
            public sealed interface Title {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : Title

              @Serializable
              @JvmInline
              public value class CaseLong(
                public val `value`: Long,
              ) : Title

              public object Serializer : KSerializer<Title> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.render.test.client.operations.inline.body.unions.Repos.OwnerPath.RepoPath.Issues.Post.Body.Title", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CaseLong", Long.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): Title {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                }

                override fun serialize(encoder: Encoder, `value`: Title) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
