package io.github.nomisrev.render.test.client.`inline`.union.case.with.nested.`enum`

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
      public val pages: Pages = Pages(client, owner, repo)

      public class Pages internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val put: Put = Put(client, owner, repo)

        public class Put internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(source: Source? = null) {
            client.put("/repos/$owner/$repo/pages") {
              contentType(ContentType.Application.Json)
              setBody(Body(source = source))
            }
          }

          @Serializable(with = Source.Serializer::class)
          public sealed interface Source {
            @Serializable
            public enum class GhPagesOrMasterOrMasterDocs(
              public val `value`: String,
            ) : Source {
              @SerialName("gh-pages")
              GhPages("gh-pages"),
              @SerialName("master")
              Master("master"),
              @SerialName("master /docs")
              MasterDocs("master /docs"),
              ;
            }

            @Serializable
            public data class BranchAndPath(
              public val branch: String,
              public val path: Path,
            ) : Source {
              @Serializable
              public enum class Path(
                public val `value`: String,
              ) {
                @SerialName("/")
                Slash("/"),
                @SerialName("/docs")
                Docs("/docs"),
                ;
              }
            }

            public object Serializer : KSerializer<Source> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.nomisrev.render.test.client.inline.union.case.with.nested.enum.Repos.OwnerPath.RepoPath.Pages.Put.Source", PolymorphicKind.SEALED) {
                element("GhPagesOrMasterOrMasterDocs", GhPagesOrMasterOrMasterDocs.serializer().descriptor)
                element("BranchAndPath", BranchAndPath.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Source {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                  value,
                  BranchAndPath::class to { decodeFromJsonElement(BranchAndPath.serializer(), it) },
                  GhPagesOrMasterOrMasterDocs::class to { decodeFromJsonElement(GhPagesOrMasterOrMasterDocs.serializer(), it) },
                )
              }

              override fun serialize(encoder: Encoder, `value`: Source) {
                when(value) {
                  is GhPagesOrMasterOrMasterDocs -> encoder.encodeSerializableValue(GhPagesOrMasterOrMasterDocs.serializer(), value)
                  is BranchAndPath -> encoder.encodeSerializableValue(BranchAndPath.serializer(), value)
                }
              }
            }
          }

          @JvmInline
          @Serializable
          internal value class Body(
            public val source: Source? = null,
          )
        }
      }
    }
  }
}
