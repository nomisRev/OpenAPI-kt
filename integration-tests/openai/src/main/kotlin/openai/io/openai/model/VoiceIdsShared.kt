package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = VoiceIdsShared.Serializer::class)
public sealed interface VoiceIdsShared {
  public val `value`: String

  @Serializable
  @JvmInline
  public value class CaseString(
    override val `value`: String,
  ) : VoiceIdsShared

  @Serializable
  public enum class AlloyOrAshOrBalladOrCoralOrEchoOrSageOrShimmerOrVerseOrMarinOrCedar(
    override val `value`: String,
  ) : VoiceIdsShared {
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

  public object Serializer : KSerializer<VoiceIdsShared> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, `value`: VoiceIdsShared) {
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

    override fun deserialize(decoder: Decoder): VoiceIdsShared = when(val value = decoder.decodeString()) {
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
