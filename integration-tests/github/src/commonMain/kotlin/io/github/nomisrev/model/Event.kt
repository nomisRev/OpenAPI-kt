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
data class Event(
    val id: String,
    val type: String?,
    val actor: Actor,
    val repo: Repo,
    val org: Actor? = null,
    val payload: Payload,
    val public: Boolean,
    @SerialName("created_at") val createdAt: LocalDateTime?,
) {
    @Serializable
    data class Repo(val id: Long, val name: String, val url: String)

    @Serializable(with = Payload.Serializer::class)
    sealed interface Payload {
        @Serializable
        @JvmInline
        value class CaseCreateEvent(val value: CreateEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseDeleteEvent(val value: DeleteEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseDiscussionEvent(val value: DiscussionEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseIssuesEvent(val value: IssuesEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseIssueCommentEvent(val value: IssueCommentEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseForkEvent(val value: ForkEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseGollumEvent(val value: GollumEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseMemberEvent(val value: MemberEvent) : Payload

        @Serializable
        @JvmInline
        value class CasePublicEvent(val value: PublicEvent) : Payload

        @Serializable
        @JvmInline
        value class CasePushEvent(val value: PushEvent) : Payload

        @Serializable
        @JvmInline
        value class CasePullRequestEvent(val value: PullRequestEvent) : Payload

        @Serializable
        @JvmInline
        value class CasePullRequestReviewCommentEvent(val value: PullRequestReviewCommentEvent) : Payload

        @Serializable
        @JvmInline
        value class CasePullRequestReviewEvent(val value: PullRequestReviewEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseCommitCommentEvent(val value: CommitCommentEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseReleaseEvent(val value: ReleaseEvent) : Payload

        @Serializable
        @JvmInline
        value class CaseWatchEvent(val value: WatchEvent) : Payload

        object Serializer : KSerializer<Payload> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Event.Payload", PolymorphicKind.SEALED) {
                    element("CaseCreateEvent", CreateEvent.serializer().descriptor)
                    element("CaseDeleteEvent", DeleteEvent.serializer().descriptor)
                    element("CaseDiscussionEvent", DiscussionEvent.serializer().descriptor)
                    element("CaseIssuesEvent", IssuesEvent.serializer().descriptor)
                    element("CaseIssueCommentEvent", IssueCommentEvent.serializer().descriptor)
                    element("CaseForkEvent", ForkEvent.serializer().descriptor)
                    element("CaseGollumEvent", GollumEvent.serializer().descriptor)
                    element("CaseMemberEvent", MemberEvent.serializer().descriptor)
                    element("CasePublicEvent", PublicEvent.serializer().descriptor)
                    element("CasePushEvent", PushEvent.serializer().descriptor)
                    element("CasePullRequestEvent", PullRequestEvent.serializer().descriptor)
                    element("CasePullRequestReviewCommentEvent", PullRequestReviewCommentEvent.serializer().descriptor)
                    element("CasePullRequestReviewEvent", PullRequestReviewEvent.serializer().descriptor)
                    element("CaseCommitCommentEvent", CommitCommentEvent.serializer().descriptor)
                    element("CaseReleaseEvent", ReleaseEvent.serializer().descriptor)
                    element("CaseWatchEvent", WatchEvent.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Payload {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseCreateEvent::class to { CaseCreateEvent(decodeFromJsonElement(CreateEvent.serializer(), it)) },
                    CaseDeleteEvent::class to { CaseDeleteEvent(decodeFromJsonElement(DeleteEvent.serializer(), it)) },
                    CaseDiscussionEvent::class to { CaseDiscussionEvent(decodeFromJsonElement(DiscussionEvent.serializer(), it)) },
                    CaseIssuesEvent::class to { CaseIssuesEvent(decodeFromJsonElement(IssuesEvent.serializer(), it)) },
                    CaseIssueCommentEvent::class to { CaseIssueCommentEvent(decodeFromJsonElement(IssueCommentEvent.serializer(), it)) },
                    CaseForkEvent::class to { CaseForkEvent(decodeFromJsonElement(ForkEvent.serializer(), it)) },
                    CaseGollumEvent::class to { CaseGollumEvent(decodeFromJsonElement(GollumEvent.serializer(), it)) },
                    CaseMemberEvent::class to { CaseMemberEvent(decodeFromJsonElement(MemberEvent.serializer(), it)) },
                    CasePublicEvent::class to { CasePublicEvent(decodeFromJsonElement(PublicEvent.serializer(), it)) },
                    CasePushEvent::class to { CasePushEvent(decodeFromJsonElement(PushEvent.serializer(), it)) },
                    CasePullRequestEvent::class to { CasePullRequestEvent(decodeFromJsonElement(PullRequestEvent.serializer(), it)) },
                    CasePullRequestReviewCommentEvent::class to { CasePullRequestReviewCommentEvent(decodeFromJsonElement(PullRequestReviewCommentEvent.serializer(), it)) },
                    CasePullRequestReviewEvent::class to { CasePullRequestReviewEvent(decodeFromJsonElement(PullRequestReviewEvent.serializer(), it)) },
                    CaseCommitCommentEvent::class to { CaseCommitCommentEvent(decodeFromJsonElement(CommitCommentEvent.serializer(), it)) },
                    CaseReleaseEvent::class to { CaseReleaseEvent(decodeFromJsonElement(ReleaseEvent.serializer(), it)) },
                    CaseWatchEvent::class to { CaseWatchEvent(decodeFromJsonElement(WatchEvent.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Payload) = when(value) {
                is CaseCreateEvent -> encoder.encodeSerializableValue(CreateEvent.serializer(), value.value)
                is CaseDeleteEvent -> encoder.encodeSerializableValue(DeleteEvent.serializer(), value.value)
                is CaseDiscussionEvent -> encoder.encodeSerializableValue(DiscussionEvent.serializer(), value.value)
                is CaseIssuesEvent -> encoder.encodeSerializableValue(IssuesEvent.serializer(), value.value)
                is CaseIssueCommentEvent -> encoder.encodeSerializableValue(IssueCommentEvent.serializer(), value.value)
                is CaseForkEvent -> encoder.encodeSerializableValue(ForkEvent.serializer(), value.value)
                is CaseGollumEvent -> encoder.encodeSerializableValue(GollumEvent.serializer(), value.value)
                is CaseMemberEvent -> encoder.encodeSerializableValue(MemberEvent.serializer(), value.value)
                is CasePublicEvent -> encoder.encodeSerializableValue(PublicEvent.serializer(), value.value)
                is CasePushEvent -> encoder.encodeSerializableValue(PushEvent.serializer(), value.value)
                is CasePullRequestEvent -> encoder.encodeSerializableValue(PullRequestEvent.serializer(), value.value)
                is CasePullRequestReviewCommentEvent -> encoder.encodeSerializableValue(PullRequestReviewCommentEvent.serializer(), value.value)
                is CasePullRequestReviewEvent -> encoder.encodeSerializableValue(PullRequestReviewEvent.serializer(), value.value)
                is CaseCommitCommentEvent -> encoder.encodeSerializableValue(CommitCommentEvent.serializer(), value.value)
                is CaseReleaseEvent -> encoder.encodeSerializableValue(ReleaseEvent.serializer(), value.value)
                is CaseWatchEvent -> encoder.encodeSerializableValue(WatchEvent.serializer(), value.value)
            }
        }
    }
}
