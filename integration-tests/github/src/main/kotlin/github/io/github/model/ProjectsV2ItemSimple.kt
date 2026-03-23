package io.github.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.Instant
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

/**
 * An item belonging to a project
 */
@Serializable
public data class ProjectsV2ItemSimple(
  public val id: Double,
  @SerialName("node_id")
  public val nodeId: String? = null,
  public val content: Content? = null,
  @SerialName("content_type")
  public val contentType: ProjectsV2ItemContentType,
  public val creator: SimpleUser? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("archived_at")
  public val archivedAt: Instant?,
  @SerialName("project_url")
  public val projectUrl: String? = null,
  @SerialName("item_url")
  public val itemUrl: String? = null,
) {
  /**
   * The content represented by the item.
   */
  @Serializable(with = Content.Serializer::class)
  public sealed interface Content {
    @Serializable
    @JvmInline
    public value class CaseIssue(
      public val `value`: Issue,
    ) : Content

    @Serializable
    @JvmInline
    public value class CasePullRequestSimple(
      public val `value`: PullRequestSimple,
    ) : Content

    @Serializable
    @JvmInline
    public value class CaseProjectsV2DraftIssue(
      public val `value`: ProjectsV2DraftIssue,
    ) : Content

    public object Serializer : KSerializer<Content> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.ProjectsV2ItemSimple.Content", PolymorphicKind.SEALED) {
        element("CaseIssue", Issue.serializer().descriptor)
        element("CasePullRequestSimple", PullRequestSimple.serializer().descriptor)
        element("CaseProjectsV2DraftIssue", ProjectsV2DraftIssue.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Content {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseIssue::class to { CaseIssue(decodeFromJsonElement(Issue.serializer(), it)) },
          CasePullRequestSimple::class to { CasePullRequestSimple(decodeFromJsonElement(PullRequestSimple.serializer(), it)) },
          CaseProjectsV2DraftIssue::class to { CaseProjectsV2DraftIssue(decodeFromJsonElement(ProjectsV2DraftIssue.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Content) {
        when(value) {
          is CaseIssue -> encoder.encodeSerializableValue(Issue.serializer(), value.value)
          is CasePullRequestSimple -> encoder.encodeSerializableValue(PullRequestSimple.serializer(), value.value)
          is CaseProjectsV2DraftIssue -> encoder.encodeSerializableValue(ProjectsV2DraftIssue.serializer(), value.value)
        }
      }
    }
  }
}
