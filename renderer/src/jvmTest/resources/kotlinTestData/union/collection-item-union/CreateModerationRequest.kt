package io.github.nomisrev.render.test.union.collection.item.union

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

@JvmInline
@Serializable
public value class CreateModerationRequest(
  public val input: Input,
) {
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
      @JvmInline
      @SerialName("image_url")
      @Serializable
      public value class ImageUrl(
        public val url: String,
      ) : ImageUrlOrText

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
          buildSerialDescriptor("io.github.nomisrev.render.test.union.collection.item.union.CreateModerationRequest.Input", PolymorphicKind.SEALED) {
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
}
