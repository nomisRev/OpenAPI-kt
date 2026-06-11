package io.github.nomisrev.render.test.union.discriminated.tagged.custom.single.tag.collision

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = MessageResource.Serializer::class)
public sealed interface MessageResource {
  @Serializable
  public data class InputMessage(
    public val type: Type,
    public val role: Role,
    public val content: String,
  ) : MessageResource {
    @Serializable
    public enum class Role(
      public val `value`: String,
    ) {
      @SerialName("user")
      User("user"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("message")
      Message("message"),
      ;
    }
  }

  @Serializable
  public data class OutputMessage(
    public val type: Type,
    public val role: Role,
    public val content: String,
    public val phase: String? = null,
  ) : MessageResource {
    @Serializable
    public enum class Role(
      public val `value`: String,
    ) {
      @SerialName("assistant")
      Assistant("assistant"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("message")
      Message("message"),
      ;
    }
  }

  public object Serializer : KSerializer<MessageResource> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.tagged.custom.single.tag.collision.MessageResource", PolymorphicKind.SEALED) {
      element("InputMessage", InputMessage.serializer().descriptor)
      element("OutputMessage", OutputMessage.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): MessageResource {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        "message" -> {
          val keys = obj?.keys.orEmpty()
          if ("phase" in keys) {
            return json.decodeFromJsonElement(OutputMessage.serializer(), value)
          }
          return json.attemptDeserialize(
            value,
            OutputMessage::class to { decodeFromJsonElement(OutputMessage.serializer(), it) },
            InputMessage::class to { decodeFromJsonElement(InputMessage.serializer(), it) },
          )
        }
        else -> throw SerializationException("Unknown tag: " + tag + " for io.github.nomisrev.render.test.union.discriminated.tagged.custom.single.tag.collision.MessageResource")
      }
    }

    override fun serialize(encoder: Encoder, `value`: MessageResource) {
      when(value) {
        is InputMessage -> encoder.encodeSerializableValue(InputMessage.serializer(), value)
        is OutputMessage -> encoder.encodeSerializableValue(OutputMessage.serializer(), value)
      }
    }
  }
}
