package io.github.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
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

/**
 * Timeline Event
 */
@Serializable(with = TimelineIssueEvents.Serializer::class)
public sealed interface TimelineIssueEvents {
  @Serializable
  @JvmInline
  public value class CaseLabeledIssueEvent(
    public val `value`: LabeledIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseUnlabeledIssueEvent(
    public val `value`: UnlabeledIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseMilestonedIssueEvent(
    public val `value`: MilestonedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseDemilestonedIssueEvent(
    public val `value`: DemilestonedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseRenamedIssueEvent(
    public val `value`: RenamedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseReviewRequestedIssueEvent(
    public val `value`: ReviewRequestedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseReviewRequestRemovedIssueEvent(
    public val `value`: ReviewRequestRemovedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseReviewDismissedIssueEvent(
    public val `value`: ReviewDismissedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseLockedIssueEvent(
    public val `value`: LockedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseAddedToProjectIssueEvent(
    public val `value`: AddedToProjectIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseMovedColumnInProjectIssueEvent(
    public val `value`: MovedColumnInProjectIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseRemovedFromProjectIssueEvent(
    public val `value`: RemovedFromProjectIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseConvertedNoteToIssueIssueEvent(
    public val `value`: ConvertedNoteToIssueIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineCommentEvent(
    public val `value`: TimelineCommentEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineCrossReferencedEvent(
    public val `value`: TimelineCrossReferencedEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineCommittedEvent(
    public val `value`: TimelineCommittedEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineReviewedEvent(
    public val `value`: TimelineReviewedEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineLineCommentedEvent(
    public val `value`: TimelineLineCommentedEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineCommitCommentedEvent(
    public val `value`: TimelineCommitCommentedEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineAssignedIssueEvent(
    public val `value`: TimelineAssignedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseTimelineUnassignedIssueEvent(
    public val `value`: TimelineUnassignedIssueEvent,
  ) : TimelineIssueEvents

  @Serializable
  @JvmInline
  public value class CaseStateChangeIssueEvent(
    public val `value`: StateChangeIssueEvent,
  ) : TimelineIssueEvents

  public object Serializer : KSerializer<TimelineIssueEvents> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.model.TimelineIssueEvents", PolymorphicKind.SEALED) {
      element("CaseLabeledIssueEvent", LabeledIssueEvent.serializer().descriptor)
      element("CaseUnlabeledIssueEvent", UnlabeledIssueEvent.serializer().descriptor)
      element("CaseMilestonedIssueEvent", MilestonedIssueEvent.serializer().descriptor)
      element("CaseDemilestonedIssueEvent", DemilestonedIssueEvent.serializer().descriptor)
      element("CaseRenamedIssueEvent", RenamedIssueEvent.serializer().descriptor)
      element("CaseReviewRequestedIssueEvent", ReviewRequestedIssueEvent.serializer().descriptor)
      element("CaseReviewRequestRemovedIssueEvent", ReviewRequestRemovedIssueEvent.serializer().descriptor)
      element("CaseReviewDismissedIssueEvent", ReviewDismissedIssueEvent.serializer().descriptor)
      element("CaseLockedIssueEvent", LockedIssueEvent.serializer().descriptor)
      element("CaseAddedToProjectIssueEvent", AddedToProjectIssueEvent.serializer().descriptor)
      element("CaseMovedColumnInProjectIssueEvent", MovedColumnInProjectIssueEvent.serializer().descriptor)
      element("CaseRemovedFromProjectIssueEvent", RemovedFromProjectIssueEvent.serializer().descriptor)
      element("CaseConvertedNoteToIssueIssueEvent", ConvertedNoteToIssueIssueEvent.serializer().descriptor)
      element("CaseTimelineCommentEvent", TimelineCommentEvent.serializer().descriptor)
      element("CaseTimelineCrossReferencedEvent", TimelineCrossReferencedEvent.serializer().descriptor)
      element("CaseTimelineCommittedEvent", TimelineCommittedEvent.serializer().descriptor)
      element("CaseTimelineReviewedEvent", TimelineReviewedEvent.serializer().descriptor)
      element("CaseTimelineLineCommentedEvent", TimelineLineCommentedEvent.serializer().descriptor)
      element("CaseTimelineCommitCommentedEvent", TimelineCommitCommentedEvent.serializer().descriptor)
      element("CaseTimelineAssignedIssueEvent", TimelineAssignedIssueEvent.serializer().descriptor)
      element("CaseTimelineUnassignedIssueEvent", TimelineUnassignedIssueEvent.serializer().descriptor)
      element("CaseStateChangeIssueEvent", StateChangeIssueEvent.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): TimelineIssueEvents {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseLabeledIssueEvent::class to { CaseLabeledIssueEvent(decodeFromJsonElement(LabeledIssueEvent.serializer(), it)) },
        CaseUnlabeledIssueEvent::class to { CaseUnlabeledIssueEvent(decodeFromJsonElement(UnlabeledIssueEvent.serializer(), it)) },
        CaseMilestonedIssueEvent::class to { CaseMilestonedIssueEvent(decodeFromJsonElement(MilestonedIssueEvent.serializer(), it)) },
        CaseDemilestonedIssueEvent::class to { CaseDemilestonedIssueEvent(decodeFromJsonElement(DemilestonedIssueEvent.serializer(), it)) },
        CaseRenamedIssueEvent::class to { CaseRenamedIssueEvent(decodeFromJsonElement(RenamedIssueEvent.serializer(), it)) },
        CaseReviewRequestedIssueEvent::class to { CaseReviewRequestedIssueEvent(decodeFromJsonElement(ReviewRequestedIssueEvent.serializer(), it)) },
        CaseReviewRequestRemovedIssueEvent::class to { CaseReviewRequestRemovedIssueEvent(decodeFromJsonElement(ReviewRequestRemovedIssueEvent.serializer(), it)) },
        CaseReviewDismissedIssueEvent::class to { CaseReviewDismissedIssueEvent(decodeFromJsonElement(ReviewDismissedIssueEvent.serializer(), it)) },
        CaseLockedIssueEvent::class to { CaseLockedIssueEvent(decodeFromJsonElement(LockedIssueEvent.serializer(), it)) },
        CaseAddedToProjectIssueEvent::class to { CaseAddedToProjectIssueEvent(decodeFromJsonElement(AddedToProjectIssueEvent.serializer(), it)) },
        CaseMovedColumnInProjectIssueEvent::class to { CaseMovedColumnInProjectIssueEvent(decodeFromJsonElement(MovedColumnInProjectIssueEvent.serializer(), it)) },
        CaseRemovedFromProjectIssueEvent::class to { CaseRemovedFromProjectIssueEvent(decodeFromJsonElement(RemovedFromProjectIssueEvent.serializer(), it)) },
        CaseConvertedNoteToIssueIssueEvent::class to { CaseConvertedNoteToIssueIssueEvent(decodeFromJsonElement(ConvertedNoteToIssueIssueEvent.serializer(), it)) },
        CaseTimelineCommentEvent::class to { CaseTimelineCommentEvent(decodeFromJsonElement(TimelineCommentEvent.serializer(), it)) },
        CaseTimelineCrossReferencedEvent::class to { CaseTimelineCrossReferencedEvent(decodeFromJsonElement(TimelineCrossReferencedEvent.serializer(), it)) },
        CaseTimelineCommittedEvent::class to { CaseTimelineCommittedEvent(decodeFromJsonElement(TimelineCommittedEvent.serializer(), it)) },
        CaseTimelineReviewedEvent::class to { CaseTimelineReviewedEvent(decodeFromJsonElement(TimelineReviewedEvent.serializer(), it)) },
        CaseTimelineLineCommentedEvent::class to { CaseTimelineLineCommentedEvent(decodeFromJsonElement(TimelineLineCommentedEvent.serializer(), it)) },
        CaseTimelineCommitCommentedEvent::class to { CaseTimelineCommitCommentedEvent(decodeFromJsonElement(TimelineCommitCommentedEvent.serializer(), it)) },
        CaseTimelineAssignedIssueEvent::class to { CaseTimelineAssignedIssueEvent(decodeFromJsonElement(TimelineAssignedIssueEvent.serializer(), it)) },
        CaseTimelineUnassignedIssueEvent::class to { CaseTimelineUnassignedIssueEvent(decodeFromJsonElement(TimelineUnassignedIssueEvent.serializer(), it)) },
        CaseStateChangeIssueEvent::class to { CaseStateChangeIssueEvent(decodeFromJsonElement(StateChangeIssueEvent.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: TimelineIssueEvents) {
      when(value) {
        is CaseLabeledIssueEvent -> encoder.encodeSerializableValue(LabeledIssueEvent.serializer(), value.value)
        is CaseUnlabeledIssueEvent -> encoder.encodeSerializableValue(UnlabeledIssueEvent.serializer(), value.value)
        is CaseMilestonedIssueEvent -> encoder.encodeSerializableValue(MilestonedIssueEvent.serializer(), value.value)
        is CaseDemilestonedIssueEvent -> encoder.encodeSerializableValue(DemilestonedIssueEvent.serializer(), value.value)
        is CaseRenamedIssueEvent -> encoder.encodeSerializableValue(RenamedIssueEvent.serializer(), value.value)
        is CaseReviewRequestedIssueEvent -> encoder.encodeSerializableValue(ReviewRequestedIssueEvent.serializer(), value.value)
        is CaseReviewRequestRemovedIssueEvent -> encoder.encodeSerializableValue(ReviewRequestRemovedIssueEvent.serializer(), value.value)
        is CaseReviewDismissedIssueEvent -> encoder.encodeSerializableValue(ReviewDismissedIssueEvent.serializer(), value.value)
        is CaseLockedIssueEvent -> encoder.encodeSerializableValue(LockedIssueEvent.serializer(), value.value)
        is CaseAddedToProjectIssueEvent -> encoder.encodeSerializableValue(AddedToProjectIssueEvent.serializer(), value.value)
        is CaseMovedColumnInProjectIssueEvent -> encoder.encodeSerializableValue(MovedColumnInProjectIssueEvent.serializer(), value.value)
        is CaseRemovedFromProjectIssueEvent -> encoder.encodeSerializableValue(RemovedFromProjectIssueEvent.serializer(), value.value)
        is CaseConvertedNoteToIssueIssueEvent -> encoder.encodeSerializableValue(ConvertedNoteToIssueIssueEvent.serializer(), value.value)
        is CaseTimelineCommentEvent -> encoder.encodeSerializableValue(TimelineCommentEvent.serializer(), value.value)
        is CaseTimelineCrossReferencedEvent -> encoder.encodeSerializableValue(TimelineCrossReferencedEvent.serializer(), value.value)
        is CaseTimelineCommittedEvent -> encoder.encodeSerializableValue(TimelineCommittedEvent.serializer(), value.value)
        is CaseTimelineReviewedEvent -> encoder.encodeSerializableValue(TimelineReviewedEvent.serializer(), value.value)
        is CaseTimelineLineCommentedEvent -> encoder.encodeSerializableValue(TimelineLineCommentedEvent.serializer(), value.value)
        is CaseTimelineCommitCommentedEvent -> encoder.encodeSerializableValue(TimelineCommitCommentedEvent.serializer(), value.value)
        is CaseTimelineAssignedIssueEvent -> encoder.encodeSerializableValue(TimelineAssignedIssueEvent.serializer(), value.value)
        is CaseTimelineUnassignedIssueEvent -> encoder.encodeSerializableValue(TimelineUnassignedIssueEvent.serializer(), value.value)
        is CaseStateChangeIssueEvent -> encoder.encodeSerializableValue(StateChangeIssueEvent.serializer(), value.value)
      }
    }
  }
}
