package io.github.model

import kotlin.Boolean
import kotlin.Long
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
 * Event
 */
@Serializable
public data class Event(
  public val id: String,
  public val type: String?,
  public val actor: Actor,
  public val repo: Repo,
  public val org: Actor? = null,
  public val payload: Payload,
  public val `public`: Boolean,
  @SerialName("created_at")
  public val createdAt: Instant?,
) {
  @Serializable(with = Payload.Serializer::class)
  public sealed interface Payload {
    @Serializable
    @JvmInline
    public value class CaseCreateEvent(
      public val `value`: CreateEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseDeleteEvent(
      public val `value`: DeleteEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseDiscussionEvent(
      public val `value`: DiscussionEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseIssuesEvent(
      public val `value`: IssuesEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseIssueCommentEvent(
      public val `value`: IssueCommentEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseForkEvent(
      public val `value`: ForkEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseGollumEvent(
      public val `value`: GollumEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseMemberEvent(
      public val `value`: MemberEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CasePublicEvent(
      public val `value`: PublicEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CasePushEvent(
      public val `value`: PushEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CasePullRequestEvent(
      public val `value`: PullRequestEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CasePullRequestReviewCommentEvent(
      public val `value`: PullRequestReviewCommentEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CasePullRequestReviewEvent(
      public val `value`: PullRequestReviewEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseCommitCommentEvent(
      public val `value`: CommitCommentEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseReleaseEvent(
      public val `value`: ReleaseEvent,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseWatchEvent(
      public val `value`: WatchEvent,
    ) : Payload

    public object Serializer : KSerializer<Payload> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Event.Payload", PolymorphicKind.SEALED) {
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

      override fun serialize(encoder: Encoder, `value`: Payload) {
        when(value) {
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

  @Serializable
  public data class Repo(
    public val id: Long,
    public val name: String,
    public val url: String,
  )
}
