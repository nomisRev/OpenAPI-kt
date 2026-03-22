package io.github.nomisrev.render.test.collection.item.with.nested.union

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

@JvmInline
@Serializable
public value class ModerationInputs(
  public val items: List<Item>,
) {
  @Serializable(with = Item.Serializer::class)
  public sealed interface Item {
    @Serializable
    public data class ImageUrl(
      public val type: Type,
      @SerialName("image_url")
      public val imageUrl: ImageUrl,
    ) : Item {
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
    ) : Item {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }
    }

    public object Serializer : KSerializer<Item> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.collection.item.with.nested.union.ModerationInputs.Item", PolymorphicKind.SEALED) {
        element("ImageUrl", ImageUrl.serializer().descriptor)
        element("Text", Text.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Item {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          ImageUrl::class to { decodeFromJsonElement(ImageUrl.serializer(), it) },
          Text::class to { decodeFromJsonElement(Text.serializer(), it) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Item) {
        when(value) {
          is ImageUrl -> encoder.encodeSerializableValue(ImageUrl.serializer(), value)
          is Text -> encoder.encodeSerializableValue(Text.serializer(), value)
        }
      }
    }
  }
}
