package io.github.api

import io.github.model.BasicError
import io.github.model.Enterprise
import io.github.model.SimpleUser
import io.github.model.ValidationErrorSimple
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

public class AppManifests internal constructor(
  private val client: HttpClient,
) {
  public fun code(code: String): CodePath = CodePath(client, code)

  public class CodePath internal constructor(
    private val client: HttpClient,
    private val code: String,
  ) {
    public val conversions: Conversions = Conversions(client, code)

    public class Conversions internal constructor(
      private val client: HttpClient,
      private val code: String,
    ) {
      public val post: Post = Post(client, code)

      public class Post internal constructor(
        private val client: HttpClient,
        private val code: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.post("/app-manifests/$code/conversions")
          return when (response.status.value) {
            201 -> response.body<Response.Created>()
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          /**
           * GitHub apps are a new way to extend GitHub. They can be installed directly on organizations and user accounts and granted access to specific repositories. They come with granular permissions and built-in webhooks. GitHub apps are first class actors within GitHub.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @KeepGeneratedSerializer
          @Serializable(with = Created.Serializer::class)
          public data class Created(
            public val id: Long,
            public val slug: String? = null,
            @SerialName("node_id")
            public val nodeId: String,
            @SerialName("client_id")
            public val clientId: String? = null,
            public val owner: Owner,
            public val name: String,
            public val description: String?,
            @SerialName("external_url")
            public val externalUrl: String,
            @SerialName("html_url")
            public val htmlUrl: String,
            @SerialName("created_at")
            public val createdAt: Instant,
            @SerialName("updated_at")
            public val updatedAt: Instant,
            public val permissions: Permissions,
            public val events: List<String>,
            @SerialName("installations_count")
            public val installationsCount: Long? = null,
            @SerialName("client_secret")
            public val clientSecret: String? = null,
            @SerialName("webhook_secret")
            public val webhookSecret: String? = null,
            public val pem: String? = null,
            public val additional: JsonObject? = null,
          ) : Response {
            @Serializable(with = Owner.Serializer::class)
            public sealed interface Owner {
              @Serializable
              @JvmInline
              public value class CaseSimpleUser(
                public val `value`: SimpleUser,
              ) : Owner

              @Serializable
              @JvmInline
              public value class CaseEnterprise(
                public val `value`: Enterprise,
              ) : Owner

              public object Serializer : KSerializer<Owner> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.AppManifests.CodePath.Conversions.Post.Response.Created.Owner", PolymorphicKind.SEALED) {
                  element("CaseSimpleUser", SimpleUser.serializer().descriptor)
                  element("CaseEnterprise", Enterprise.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): Owner {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return json.attemptDeserialize(
                    value,
                    CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
                    CaseEnterprise::class to { CaseEnterprise(decodeFromJsonElement(Enterprise.serializer(), it)) },
                  )
                }

                override fun serialize(encoder: Encoder, `value`: Owner) {
                  when(value) {
                    is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
                    is CaseEnterprise -> encoder.encodeSerializableValue(Enterprise.serializer(), value.value)
                  }
                }
              }
            }

            /**
             * The set of permissions for the GitHub app
             */
            @OptIn(ExperimentalSerializationApi::class)
            @KeepGeneratedSerializer
            @Serializable(with = Permissions.Serializer::class)
            public data class Permissions(
              public val issues: String? = null,
              public val checks: String? = null,
              public val metadata: String? = null,
              public val contents: String? = null,
              public val deployments: String? = null,
              public val additional: Map<String, String>? = null,
            ) {
              public object Serializer : KSerializer<Permissions> {
                override val descriptor: SerialDescriptor = generatedSerializer().descriptor

                override fun serialize(encoder: Encoder, `value`: Permissions) {
                  val json = (encoder as JsonEncoder).json
                  val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
                  val content = mutableMapOf<String, JsonElement>()
                  known.forEach {
                    if (it.key != "additional") {
                      content[it.key] = it.value
                    }
                  }
                  value.additional?.forEach {
                    content[it.key] = json.encodeToJsonElement(String.serializer(), it.value)
                  }
                  encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
                }

                override fun deserialize(decoder: Decoder): Permissions {
                  val json = (decoder as JsonDecoder).json
                  val element = decoder.decodeSerializableValue(JsonObject.serializer())
                  val knownNames = setOf("issues", "checks", "metadata", "contents", "deployments")
                  val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
                  val additional = (element - knownNames)
                    .mapValues { json.decodeFromJsonElement(String.serializer(), it.value) }
                    .ifEmpty { null }
                  return known.copy(additional = additional)
                }
              }
            }

            public object Serializer : KSerializer<Created> {
              override val descriptor: SerialDescriptor = generatedSerializer().descriptor

              override fun serialize(encoder: Encoder, `value`: Created) {
                val json = (encoder as JsonEncoder).json
                val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
                val content = mutableMapOf<String, JsonElement>()
                known.forEach {
                  if (it.key != "additional") {
                    content[it.key] = it.value
                  }
                }
                value.additional?.forEach {
                  content[it.key] = it.value
                }
                encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
              }

              override fun deserialize(decoder: Decoder): Created {
                val json = (decoder as JsonDecoder).json
                val element = decoder.decodeSerializableValue(JsonObject.serializer())
                val knownNames = setOf("id", "slug", "node_id", "client_id", "owner", "name", "description", "external_url", "html_url", "created_at", "updated_at", "permissions", "events", "installations_count", "client_secret", "webhook_secret", "pem")
                val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
                val additional = JsonObject(element - knownNames).ifEmpty { null }
                return known.copy(additional = additional)
              }
            }
          }

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationErrorSimple,
          ) : Response
        }
      }
    }
  }
}
