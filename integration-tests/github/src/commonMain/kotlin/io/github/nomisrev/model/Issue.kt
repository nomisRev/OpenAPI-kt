package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class Issue(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    @SerialName("repository_url") val repositoryUrl: String,
    @SerialName("labels_url") val labelsUrl: String,
    @SerialName("comments_url") val commentsUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    val number: Long,
    val state: String,
    @SerialName("state_reason") val stateReason: StateReason? = null,
    val title: String,
    val body: String? = null,
    val user: NullableSimpleUser?,
    val labels: List<Labels>,
    val assignee: NullableSimpleUser?,
    val assignees: List<SimpleUser>? = null,
    val milestone: NullableMilestone?,
    val locked: Boolean,
    @SerialName("active_lock_reason") val activeLockReason: String? = null,
    val comments: Long,
    @SerialName("pull_request") val pullRequest: PullRequest? = null,
    @SerialName("closed_at") val closedAt: LocalDateTime?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val draft: Boolean? = null,
    @SerialName("closed_by") val closedBy: NullableSimpleUser? = null,
    @SerialName("body_html") val bodyHtml: String? = null,
    @SerialName("body_text") val bodyText: String? = null,
    @SerialName("timeline_url") val timelineUrl: String? = null,
    val type: IssueType? = null,
    val repository: Repository? = null,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration? = null,
    @SerialName("author_association") val authorAssociation: AuthorAssociation? = null,
    val reactions: ReactionRollup? = null,
    @SerialName("sub_issues_summary") val subIssuesSummary: SubIssuesSummary? = null,
    @SerialName("parent_issue_url") val parentIssueUrl: String? = null,
    @SerialName("pinned_comment") val pinnedComment: NullableIssueComment? = null,
    @SerialName("issue_dependencies_summary") val issueDependenciesSummary: IssueDependenciesSummary? = null,
    @SerialName("issue_field_values") val issueFieldValues: List<IssueFieldValue>? = null,
) {
    @Serializable
    enum class StateReason {
        @SerialName("completed")
        Completed,
        @SerialName("reopened")
        Reopened,
        @SerialName("not_planned")
        NotPlanned,
        @SerialName("duplicate")
        Duplicate;
    }

    @Serializable(with = Labels.Serializer::class)
    sealed interface Labels {
        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Labels

        @Serializable
        data class IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault(
            val id: Long? = null,
            @SerialName("node_id") val nodeId: String? = null,
            val url: String? = null,
            val name: String? = null,
            val description: String? = null,
            val color: String? = null,
            val default: Boolean? = null,
        ) : Labels

        object Serializer : KSerializer<Labels> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Issue.Labels", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault", Labels.IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Labels {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault::class to { decodeFromJsonElement(Labels.IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault.serializer(), it) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Labels) = when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault -> encoder.encodeSerializableValue(Labels.IdAndNodeIdAndUrlAndNameAndDescriptionAndColorAndDefault.serializer(), value)
            }
        }
    }

    @Serializable
    data class PullRequest(
        @SerialName("merged_at") val mergedAt: LocalDateTime? = null,
        @SerialName("diff_url") val diffUrl: String?,
        @SerialName("html_url") val htmlUrl: String?,
        @SerialName("patch_url") val patchUrl: String?,
        val url: String?,
    )
}
