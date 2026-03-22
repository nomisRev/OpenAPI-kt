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
    public value class CaseThreeList(
      public val `value`: List<Three>,
    ) : Input

    @Serializable(with = Three.Serializer::class)
    public sealed interface Three {
      @Serializable
      public data class ImageUrl(
        public val type: Type,
        @SerialName("image_url")
        public val imageUrl: ImageUrl,
      ) : Three {
        @JvmInline
        @Serializable
        public value class ImageUrl(
          public val url: String,
        )

        @Serializable
        public enum class Type(
          public val `value`: String,
        ) {
          @SerialName("image_url")
          ImageUrl("image_url"),
          ;
        }
      }

      @Serializable
      public data class Text(
        public val type: Type,
        public val text: String,
      ) : Three {
        @Serializable
        public enum class Type(
          public val `value`: String,
        ) {
          @SerialName("text")
          Text("text"),
          ;
        }
      }

      public object Serializer : KSerializer<Three> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.render.test.union.collection.item.union.CreateModerationRequest.Input.Three", PolymorphicKind.SEALED) {
          element("ImageUrl", ImageUrl.serializer().descriptor)
          element("Text", Text.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): Three {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            ImageUrl::class to { decodeFromJsonElement(ImageUrl.serializer(), it) },
            Text::class to { decodeFromJsonElement(Text.serializer(), it) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Three) {
          when(value) {
            is ImageUrl -> encoder.encodeSerializableValue(ImageUrl.serializer(), value)
            is Text -> encoder.encodeSerializableValue(Text.serializer(), value)
          }
        }
      }
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
        element("CaseThreeList", ListSerializer(Three.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Input {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
          CaseThreeList::class to { CaseThreeList(decodeFromJsonElement(ListSerializer(Three.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Input) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          is CaseThreeList -> encoder.encodeSerializableValue(ListSerializer(Three.serializer()), value.value)
        }
      }
    }
  }
}
