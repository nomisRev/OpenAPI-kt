package io.github.nomisrev.render.test.client.`inline`.oneof.request.body.collection.`inline`.items

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public interface Repos {
  public fun owner(owner: String): OwnerPath

  public interface OwnerPath {
    public fun repo(repo: String): RepoPath

    public interface RepoPath {
      public val issues: Issues

      public interface Issues {
        public fun issueNumber(issueNumber: Long): IssueNumberPath

        public interface IssueNumberPath {
          public val labels: Labels

          public interface Labels {
            public val put: Put

            public interface Put {
              public suspend operator fun invoke(body: Body? = null): Response

              @Serializable(with = Body.Serializer::class)
              public sealed interface Body {
                @JvmInline
                @Serializable
                public value class LabelsStrings(
                  public val labels: List<String>? = null,
                ) : Body

                @Serializable
                @JvmInline
                public value class CaseStrings(
                  public val `value`: List<String>,
                ) : Body

                @JvmInline
                @Serializable
                public value class LabelsNames(
                  public val labels: List<Labels>? = null,
                ) : Body {
                  @JvmInline
                  @Serializable
                  public value class Labels(
                    public val name: String,
                  )
                }

                @Serializable
                @JvmInline
                public value class CaseNameList(
                  public val `value`: List<Name>,
                ) : Body

                @Serializable
                @JvmInline
                public value class CaseString(
                  public val `value`: String,
                ) : Body

                @JvmInline
                @Serializable
                public value class Name(
                  public val name: String,
                )

                public object Serializer : KSerializer<Body> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.nomisrev.render.test.client.inline.oneof.request.body.collection.inline.items.Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Body", PolymorphicKind.SEALED) {
                    element("LabelsStrings", LabelsStrings.serializer().descriptor)
                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                    element("LabelsNames", LabelsNames.serializer().descriptor)
                    element("CaseNameList", ListSerializer(Name.serializer()).descriptor)
                    element("CaseString", String.serializer().descriptor)
                  }

                  override fun deserialize(decoder: Decoder): Body {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                      value,
                      LabelsStrings::class to { decodeFromJsonElement(LabelsStrings.serializer(), it) },
                      LabelsNames::class to { decodeFromJsonElement(LabelsNames.serializer(), it) },
                      CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                      CaseNameList::class to { CaseNameList(decodeFromJsonElement(ListSerializer(Name.serializer()), it)) },
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                  }

                  override fun serialize(encoder: Encoder, `value`: Body) {
                    when(value) {
                      is LabelsStrings -> encoder.encodeSerializableValue(LabelsStrings.serializer(), value)
                      is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                      is LabelsNames -> encoder.encodeSerializableValue(LabelsNames.serializer(), value)
                      is CaseNameList -> encoder.encodeSerializableValue(ListSerializer(Name.serializer()), value.value)
                      is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    }
                  }
                }
              }

              public data class Response(
                public val `value`: List<Response>,
              )
            }
          }
        }
      }
    }
  }
}

internal class KtorRepos(
  private val client: HttpClient,
) : Repos {
  override fun owner(owner: String): Repos.OwnerPath = KtorReposOwnerPath(client, owner)
}

internal class KtorReposOwnerPath(
  private val client: HttpClient,
  private val owner: String,
) : Repos.OwnerPath {
  override fun repo(repo: String): Repos.OwnerPath.RepoPath = KtorReposOwnerPathRepoPath(client, owner, repo)
}

internal class KtorReposOwnerPathRepoPath(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
) : Repos.OwnerPath.RepoPath {
  override val issues: Repos.OwnerPath.RepoPath.Issues =
      KtorReposOwnerPathRepoPathIssues(client, owner, repo)
}

internal class KtorReposOwnerPathRepoPathIssues(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
) : Repos.OwnerPath.RepoPath.Issues {
  override fun issueNumber(issueNumber: Long): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath = KtorReposOwnerPathRepoPathIssuesIssueNumberPath(client, owner, repo, issueNumber)
}

internal class KtorReposOwnerPathRepoPathIssuesIssueNumberPath(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
  private val issueNumber: Long,
) : Repos.OwnerPath.RepoPath.Issues.IssueNumberPath {
  override val labels: Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels =
      KtorReposOwnerPathRepoPathIssuesIssueNumberPathLabels(client, owner, repo, issueNumber)
}

internal class KtorReposOwnerPathRepoPathIssuesIssueNumberPathLabels(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
  private val issueNumber: Long,
) : Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels {
  override val put: Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put =
      object : Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put {
    override suspend operator fun invoke(body: Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Body?): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response {
      val value: List<Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response> = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
      return Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response(value)
    }
  }
}
