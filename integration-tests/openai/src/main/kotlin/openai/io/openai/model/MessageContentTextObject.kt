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
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * The text content that is part of a message.
 */
@Serializable
public data class MessageContentTextObject(
  public val type: Type,
  public val text: Text,
) {
  @Serializable
  public data class Text(
    public val `value`: String,
    public val annotations: List<Annotations>,
  ) {
    @Serializable(with = Annotations.Serializer::class)
    public sealed interface Annotations {
      @Serializable
      @JvmInline
      public value class CaseMessageContentTextAnnotationsFileCitationObject(
        public val `value`: MessageContentTextAnnotationsFileCitationObject,
      ) : Annotations

      @Serializable
      @JvmInline
      public value class CaseMessageContentTextAnnotationsFilePathObject(
        public val `value`: MessageContentTextAnnotationsFilePathObject,
      ) : Annotations

      public object Serializer : KSerializer<Annotations> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.MessageContentTextObject.Text.Annotations", PolymorphicKind.SEALED) {
          element("CaseMessageContentTextAnnotationsFileCitationObject", MessageContentTextAnnotationsFileCitationObject.serializer().descriptor)
          element("CaseMessageContentTextAnnotationsFilePathObject", MessageContentTextAnnotationsFilePathObject.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): Annotations {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseMessageContentTextAnnotationsFileCitationObject::class to { CaseMessageContentTextAnnotationsFileCitationObject(decodeFromJsonElement(MessageContentTextAnnotationsFileCitationObject.serializer(), it)) },
            CaseMessageContentTextAnnotationsFilePathObject::class to { CaseMessageContentTextAnnotationsFilePathObject(decodeFromJsonElement(MessageContentTextAnnotationsFilePathObject.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Annotations) {
          when(value) {
            is CaseMessageContentTextAnnotationsFileCitationObject -> encoder.encodeSerializableValue(MessageContentTextAnnotationsFileCitationObject.serializer(), value.value)
            is CaseMessageContentTextAnnotationsFilePathObject -> encoder.encodeSerializableValue(MessageContentTextAnnotationsFilePathObject.serializer(), value.value)
          }
        }
      }
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    ;
  }
}
