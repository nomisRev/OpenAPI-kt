package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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

@Serializable
public data class UsageTimeBucket(
  public val `object`: Object,
  @SerialName("start_time")
  public val startTime: Long,
  @SerialName("end_time")
  public val endTime: Long,
  public val result: List<Result>,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("bucket")
    Bucket("bucket"),
    ;
  }

  @Serializable(with = Result.Serializer::class)
  public sealed interface Result {
    @Serializable
    @JvmInline
    public value class CaseUsageCompletionsResult(
      public val `value`: UsageCompletionsResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseUsageEmbeddingsResult(
      public val `value`: UsageEmbeddingsResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseUsageModerationsResult(
      public val `value`: UsageModerationsResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseUsageImagesResult(
      public val `value`: UsageImagesResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseUsageAudioSpeechesResult(
      public val `value`: UsageAudioSpeechesResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseUsageAudioTranscriptionsResult(
      public val `value`: UsageAudioTranscriptionsResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseUsageVectorStoresResult(
      public val `value`: UsageVectorStoresResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseUsageCodeInterpreterSessionsResult(
      public val `value`: UsageCodeInterpreterSessionsResult,
    ) : Result

    @Serializable
    @JvmInline
    public value class CaseCostsResult(
      public val `value`: CostsResult,
    ) : Result

    public object Serializer : KSerializer<Result> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.UsageTimeBucket.Result", PolymorphicKind.SEALED) {
        element("CaseUsageCompletionsResult", UsageCompletionsResult.serializer().descriptor)
        element("CaseUsageEmbeddingsResult", UsageEmbeddingsResult.serializer().descriptor)
        element("CaseUsageModerationsResult", UsageModerationsResult.serializer().descriptor)
        element("CaseUsageImagesResult", UsageImagesResult.serializer().descriptor)
        element("CaseUsageAudioSpeechesResult", UsageAudioSpeechesResult.serializer().descriptor)
        element("CaseUsageAudioTranscriptionsResult", UsageAudioTranscriptionsResult.serializer().descriptor)
        element("CaseUsageVectorStoresResult", UsageVectorStoresResult.serializer().descriptor)
        element("CaseUsageCodeInterpreterSessionsResult", UsageCodeInterpreterSessionsResult.serializer().descriptor)
        element("CaseCostsResult", CostsResult.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Result {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseUsageCompletionsResult::class to { CaseUsageCompletionsResult(decodeFromJsonElement(UsageCompletionsResult.serializer(), it)) },
          CaseUsageEmbeddingsResult::class to { CaseUsageEmbeddingsResult(decodeFromJsonElement(UsageEmbeddingsResult.serializer(), it)) },
          CaseUsageModerationsResult::class to { CaseUsageModerationsResult(decodeFromJsonElement(UsageModerationsResult.serializer(), it)) },
          CaseUsageImagesResult::class to { CaseUsageImagesResult(decodeFromJsonElement(UsageImagesResult.serializer(), it)) },
          CaseUsageAudioSpeechesResult::class to { CaseUsageAudioSpeechesResult(decodeFromJsonElement(UsageAudioSpeechesResult.serializer(), it)) },
          CaseUsageAudioTranscriptionsResult::class to { CaseUsageAudioTranscriptionsResult(decodeFromJsonElement(UsageAudioTranscriptionsResult.serializer(), it)) },
          CaseUsageVectorStoresResult::class to { CaseUsageVectorStoresResult(decodeFromJsonElement(UsageVectorStoresResult.serializer(), it)) },
          CaseUsageCodeInterpreterSessionsResult::class to { CaseUsageCodeInterpreterSessionsResult(decodeFromJsonElement(UsageCodeInterpreterSessionsResult.serializer(), it)) },
          CaseCostsResult::class to { CaseCostsResult(decodeFromJsonElement(CostsResult.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Result) {
        when(value) {
          is CaseUsageCompletionsResult -> encoder.encodeSerializableValue(UsageCompletionsResult.serializer(), value.value)
          is CaseUsageEmbeddingsResult -> encoder.encodeSerializableValue(UsageEmbeddingsResult.serializer(), value.value)
          is CaseUsageModerationsResult -> encoder.encodeSerializableValue(UsageModerationsResult.serializer(), value.value)
          is CaseUsageImagesResult -> encoder.encodeSerializableValue(UsageImagesResult.serializer(), value.value)
          is CaseUsageAudioSpeechesResult -> encoder.encodeSerializableValue(UsageAudioSpeechesResult.serializer(), value.value)
          is CaseUsageAudioTranscriptionsResult -> encoder.encodeSerializableValue(UsageAudioTranscriptionsResult.serializer(), value.value)
          is CaseUsageVectorStoresResult -> encoder.encodeSerializableValue(UsageVectorStoresResult.serializer(), value.value)
          is CaseUsageCodeInterpreterSessionsResult -> encoder.encodeSerializableValue(UsageCodeInterpreterSessionsResult.serializer(), value.value)
          is CaseCostsResult -> encoder.encodeSerializableValue(CostsResult.serializer(), value.value)
        }
      }
    }
  }
}
