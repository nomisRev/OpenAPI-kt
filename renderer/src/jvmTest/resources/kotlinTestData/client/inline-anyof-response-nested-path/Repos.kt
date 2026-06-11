package io.github.nomisrev.render.test.client.`inline`.anyof.response.nested.path

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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
      public val interactionLimits: InteractionLimits = InteractionLimits(client, owner, repo)

      public class InteractionLimits internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response = client.get("/repos/$owner/$repo/interaction-limits").body()

          @Serializable(with = Response.Serializer::class)
          public sealed interface Response {
            @Serializable
            public data class LimitAndOrigin(
              public val limit: String,
              public val origin: String,
            ) : Response

            @Serializable
            public data object Empty : Response

            public object Serializer : KSerializer<Response> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.nomisrev.render.test.client.inline.anyof.response.nested.path.Repos.OwnerPath.RepoPath.InteractionLimits.Get.Response", PolymorphicKind.SEALED) {
                element("LimitAndOrigin", LimitAndOrigin.serializer().descriptor)
                element("Empty", Empty.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Response {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                  value,
                  LimitAndOrigin::class to { decodeFromJsonElement(LimitAndOrigin.serializer(), it) },
                  Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
                )
              }

              override fun serialize(encoder: Encoder, `value`: Response) {
                when(value) {
                  is LimitAndOrigin -> encoder.encodeSerializableValue(LimitAndOrigin.serializer(), value)
                  is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
                }
              }
            }
          }
        }
      }
    }
  }
}
