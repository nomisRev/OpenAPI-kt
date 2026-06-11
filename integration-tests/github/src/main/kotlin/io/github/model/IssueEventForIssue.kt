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
 * Issue Event for Issue
 */
@Serializable(with = IssueEventForIssue.Serializer::class)
public sealed interface IssueEventForIssue {
  @Serializable
  @JvmInline
  public value class CaseLabeledIssueEvent(
    public val `value`: LabeledIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseUnlabeledIssueEvent(
    public val `value`: UnlabeledIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseAssignedIssueEvent(
    public val `value`: AssignedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseUnassignedIssueEvent(
    public val `value`: UnassignedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseMilestonedIssueEvent(
    public val `value`: MilestonedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseDemilestonedIssueEvent(
    public val `value`: DemilestonedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseRenamedIssueEvent(
    public val `value`: RenamedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseReviewRequestedIssueEvent(
    public val `value`: ReviewRequestedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseReviewRequestRemovedIssueEvent(
    public val `value`: ReviewRequestRemovedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseReviewDismissedIssueEvent(
    public val `value`: ReviewDismissedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseLockedIssueEvent(
    public val `value`: LockedIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseAddedToProjectIssueEvent(
    public val `value`: AddedToProjectIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseMovedColumnInProjectIssueEvent(
    public val `value`: MovedColumnInProjectIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseRemovedFromProjectIssueEvent(
    public val `value`: RemovedFromProjectIssueEvent,
  ) : IssueEventForIssue

  @Serializable
  @JvmInline
  public value class CaseConvertedNoteToIssueIssueEvent(
    public val `value`: ConvertedNoteToIssueIssueEvent,
  ) : IssueEventForIssue

  public object Serializer : KSerializer<IssueEventForIssue> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.model.IssueEventForIssue", PolymorphicKind.SEALED) {
      element("CaseLabeledIssueEvent", LabeledIssueEvent.serializer().descriptor)
      element("CaseUnlabeledIssueEvent", UnlabeledIssueEvent.serializer().descriptor)
      element("CaseAssignedIssueEvent", AssignedIssueEvent.serializer().descriptor)
      element("CaseUnassignedIssueEvent", UnassignedIssueEvent.serializer().descriptor)
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
    }

    override fun deserialize(decoder: Decoder): IssueEventForIssue {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseLabeledIssueEvent::class to { CaseLabeledIssueEvent(decodeFromJsonElement(LabeledIssueEvent.serializer(), it)) },
        CaseUnlabeledIssueEvent::class to { CaseUnlabeledIssueEvent(decodeFromJsonElement(UnlabeledIssueEvent.serializer(), it)) },
        CaseAssignedIssueEvent::class to { CaseAssignedIssueEvent(decodeFromJsonElement(AssignedIssueEvent.serializer(), it)) },
        CaseUnassignedIssueEvent::class to { CaseUnassignedIssueEvent(decodeFromJsonElement(UnassignedIssueEvent.serializer(), it)) },
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
      )
    }

    override fun serialize(encoder: Encoder, `value`: IssueEventForIssue) {
      when(value) {
        is CaseLabeledIssueEvent -> encoder.encodeSerializableValue(LabeledIssueEvent.serializer(), value.value)
        is CaseUnlabeledIssueEvent -> encoder.encodeSerializableValue(UnlabeledIssueEvent.serializer(), value.value)
        is CaseAssignedIssueEvent -> encoder.encodeSerializableValue(AssignedIssueEvent.serializer(), value.value)
        is CaseUnassignedIssueEvent -> encoder.encodeSerializableValue(UnassignedIssueEvent.serializer(), value.value)
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
      }
    }
  }
}
