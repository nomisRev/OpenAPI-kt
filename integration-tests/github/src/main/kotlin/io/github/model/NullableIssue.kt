package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Issues are a great way to keep track of tasks, enhancements, and bugs for your projects.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NullableIssue(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  @SerialName("labels_url")
  public val labelsUrl: String,
  @SerialName("comments_url")
  public val commentsUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val number: Long,
  public val state: String,
  @SerialName("state_reason")
  public val stateReason: StateReason? = null,
  public val title: String,
  public val body: String? = null,
  public val user: NullableSimpleUser?,
  public val labels: List<Labels>,
  public val assignee: NullableSimpleUser?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignees: List<SimpleUser>? = null,
  public val milestone: NullableMilestone?,
  public val locked: Boolean,
  @SerialName("active_lock_reason")
  public val activeLockReason: String? = null,
  public val comments: Long,
  @SerialName("pull_request")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pullRequest: PullRequest? = null,
  @SerialName("closed_at")
  public val closedAt: Instant?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val draft: Boolean? = null,
  @SerialName("closed_by")
  public val closedBy: NullableSimpleUser? = null,
  @SerialName("body_html")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyHtml: String? = null,
  @SerialName("body_text")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyText: String? = null,
  @SerialName("timeline_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timelineUrl: String? = null,
  public val type: IssueType? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repository: Repository? = null,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: NullableIntegration? = null,
  @SerialName("author_association")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val authorAssociation: AuthorAssociation? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reactions: ReactionRollup? = null,
  @SerialName("sub_issues_summary")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val subIssuesSummary: SubIssuesSummary? = null,
  @SerialName("parent_issue_url")
  public val parentIssueUrl: String? = null,
  @SerialName("pinned_comment")
  public val pinnedComment: NullableIssueComment? = null,
  @SerialName("issue_dependencies_summary")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issueDependenciesSummary: IssueDependenciesSummary? = null,
  @SerialName("issue_field_values")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issueFieldValues: List<IssueFieldValue>? = null,
) {
  @Serializable(with = Labels.Serializer::class)
  public sealed interface Labels {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Labels

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: Long? = null,
      @SerialName("node_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val nodeId: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val url: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      public val description: String? = null,
      public val color: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val default: Boolean? = null,
    ) : Labels

    public object Serializer : KSerializer<Labels> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.NullableIssue.Labels", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault", IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Labels {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonObject -> json.attemptDeserialize(
            value,
            IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault::class to { decodeFromJsonElement(IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault.serializer(), it) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Labels) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault -> encoder.encodeSerializableValue(IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault.serializer(), value)
        }
      }
    }
  }

  @Serializable
  public data class PullRequest(
    @SerialName("merged_at")
    public val mergedAt: Instant? = null,
    @SerialName("diff_url")
    public val diffUrl: String?,
    @SerialName("html_url")
    public val htmlUrl: String?,
    @SerialName("patch_url")
    public val patchUrl: String?,
    public val url: String?,
  )

  @Serializable
  public enum class StateReason(
    public val `value`: String,
  ) {
    @SerialName("completed")
    Completed("completed"),
    @SerialName("reopened")
    Reopened("reopened"),
    @SerialName("not_planned")
    NotPlanned("not_planned"),
    @SerialName("duplicate")
    Duplicate("duplicate"),
    ;
  }
}
