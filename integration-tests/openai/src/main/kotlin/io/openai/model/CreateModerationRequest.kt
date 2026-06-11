package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateModerationRequest(
  public val input: Input,
  public val model: Model? = null,
) {
  /**
   * Input (or inputs) to classify. Can be a single string, an array of strings, or
   * an array of multi-modal input objects similar to other models.
   *
   */
  @Serializable(with = Input.Serializer::class)
  public sealed interface Input {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Input

    @Serializable
    @JvmInline
    public value class CaseStrings(
      public val `value`: List<String>,
    ) : Input

    @Serializable
    @JvmInline
    public value class CaseImageUrlOrTextList(
      public val `value`: List<ImageUrlOrText>,
    ) : Input

    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface ImageUrlOrText {
      /**
       * An object describing an image to classify.
       */
      @JvmInline
      @SerialName("image_url")
      @Serializable
      public value class ImageUrl(
        public val url: String,
      ) : ImageUrlOrText

      /**
       * An object describing text to classify.
       */
      @JvmInline
      @SerialName("text")
      @Serializable
      public value class Text(
        public val text: String,
      ) : ImageUrlOrText
    }

    public object Serializer : KSerializer<Input> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateModerationRequest.Input", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        element("CaseImageUrlOrTextList", ListSerializer(ImageUrlOrText.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Input {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
          CaseImageUrlOrTextList::class to { CaseImageUrlOrTextList(decodeFromJsonElement(ListSerializer(ImageUrlOrText.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Input) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          is CaseImageUrlOrTextList -> encoder.encodeSerializableValue(ListSerializer(ImageUrlOrText.serializer()), value.value)
        }
      }
    }
  }

  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class CaseEnum(
      override val `value`: String,
    ) : Model {
      @SerialName("omni-moderation-latest")
      OmniModerationLatest("omni-moderation-latest"),
      @SerialName("omni-moderation-2024-09-26")
      OmniModeration20240926("omni-moderation-2024-09-26"),
      @SerialName("text-moderation-latest")
      TextModerationLatest("text-moderation-latest"),
      @SerialName("text-moderation-stable")
      TextModerationStable("text-moderation-stable"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          CaseEnum.OmniModerationLatest -> encoder.encodeString("omni-moderation-latest")
          CaseEnum.OmniModeration20240926 -> encoder.encodeString("omni-moderation-2024-09-26")
          CaseEnum.TextModerationLatest -> encoder.encodeString("text-moderation-latest")
          CaseEnum.TextModerationStable -> encoder.encodeString("text-moderation-stable")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "omni-moderation-latest" -> CaseEnum.OmniModerationLatest
        "omni-moderation-2024-09-26" -> CaseEnum.OmniModeration20240926
        "text-moderation-latest" -> CaseEnum.TextModerationLatest
        "text-moderation-stable" -> CaseEnum.TextModerationStable
        else -> CaseString(value)
      }
    }
  }
}
