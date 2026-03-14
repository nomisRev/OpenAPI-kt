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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class ProjectsV2ItemSimple(
    val id: Double,
    @SerialName("node_id") val nodeId: String? = null,
    val content: Content? = null,
    @SerialName("content_type") val contentType: ProjectsV2ItemContentType,
    val creator: SimpleUser? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("archived_at") val archivedAt: LocalDateTime?,
    @SerialName("project_url") val projectUrl: String? = null,
    @SerialName("item_url") val itemUrl: String? = null,
) {
    @Serializable(with = Content.Serializer::class)
    sealed interface Content {
        @Serializable
        @JvmInline
        value class CaseIssue(val value: Issue) : Content

        @Serializable
        @JvmInline
        value class CasePullRequestSimple(val value: PullRequestSimple) : Content

        @Serializable
        @JvmInline
        value class CaseProjectsV2DraftIssue(val value: ProjectsV2DraftIssue) : Content

        object Serializer : KSerializer<Content> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.ProjectsV2ItemSimple.Content", PolymorphicKind.SEALED) {
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

            override fun serialize(encoder: Encoder, value: Content) = when(value) {
                is CaseIssue -> encoder.encodeSerializableValue(Issue.serializer(), value.value)
                is CasePullRequestSimple -> encoder.encodeSerializableValue(PullRequestSimple.serializer(), value.value)
                is CaseProjectsV2DraftIssue -> encoder.encodeSerializableValue(ProjectsV2DraftIssue.serializer(), value.value)
            }
        }
    }
}
