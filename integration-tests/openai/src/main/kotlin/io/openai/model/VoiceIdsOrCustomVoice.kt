package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
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

/**
 * A built-in voice name or a custom voice reference.
 *
 */
@Serializable(with = VoiceIdsOrCustomVoice.Serializer::class)
public sealed interface VoiceIdsOrCustomVoice {
  @Serializable(with = CaseString.Serializer::class)
  public sealed interface CaseString : VoiceIdsOrCustomVoice {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : VoiceIdsOrCustomVoice.CaseString

    @Serializable
    public enum class AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar(
      override val `value`: String,
    ) : VoiceIdsOrCustomVoice.CaseString {
      @SerialName("alloy")
      Alloy("alloy"),
      @SerialName("ash")
      Ash("ash"),
      @SerialName("ballad")
      Ballad("ballad"),
      @SerialName("coral")
      Coral("coral"),
      @SerialName("echo")
      Echo("echo"),
      @SerialName("sage")
      Sage("sage"),
      @SerialName("shimmer")
      Shimmer("shimmer"),
      @SerialName("verse")
      Verse("verse"),
      @SerialName("marin")
      Marin("marin"),
      @SerialName("cedar")
      Cedar("cedar"),
      ;
    }

    public object Serializer : KSerializer<VoiceIdsOrCustomVoice.CaseString> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: VoiceIdsOrCustomVoice.CaseString) {
        when(value) {
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Alloy -> encoder.encodeString("alloy")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Ash -> encoder.encodeString("ash")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Ballad -> encoder.encodeString("ballad")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Coral -> encoder.encodeString("coral")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Echo -> encoder.encodeString("echo")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Sage -> encoder.encodeString("sage")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Shimmer -> encoder.encodeString("shimmer")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Verse -> encoder.encodeString("verse")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Marin -> encoder.encodeString("marin")
          AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Cedar -> encoder.encodeString("cedar")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): VoiceIdsOrCustomVoice.CaseString = when(val value = decoder.decodeString()) {
        "alloy" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Alloy
        "ash" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Ash
        "ballad" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Ballad
        "coral" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Coral
        "echo" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Echo
        "sage" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Sage
        "shimmer" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Shimmer
        "verse" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Verse
        "marin" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Marin
        "cedar" -> AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar.Cedar
        else -> CaseString(value)
      }
    }
  }

  /**
   * Custom voice reference.
   */
  @JvmInline
  @Serializable
  public value class Id(
    public val id: String,
  ) : VoiceIdsOrCustomVoice

  public object Serializer : KSerializer<VoiceIdsOrCustomVoice> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.VoiceIdsOrCustomVoice", PolymorphicKind.SEALED) {
      element("CaseString", CaseString.serializer().descriptor)
      element("Id", Id.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): VoiceIdsOrCustomVoice {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        Id::class to { decodeFromJsonElement(Id.serializer(), it) },
        CaseString::class to { decodeFromJsonElement(CaseString.serializer(), it) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: VoiceIdsOrCustomVoice) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(CaseString.serializer(), value)
        is Id -> encoder.encodeSerializableValue(Id.serializer(), value)
      }
    }
  }
}
