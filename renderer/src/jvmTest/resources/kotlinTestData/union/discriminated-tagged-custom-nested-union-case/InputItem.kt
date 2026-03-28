package io.github.nomisrev.render.test.union.discriminated.tagged.custom.nested.union.case

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
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
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = InputItem.Serializer::class)
public sealed interface InputItem {
  @Serializable
  public data class Message(
    public val type: Type,
    public val role: String,
    public val content: String,
  ) : InputItem {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("message")
      Message("message"),
      ;
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Item : InputItem {
    @JvmInline
    @SerialName("message")
    @Serializable
    public value class Message(
      public val content: String,
    ) : Item

    @JvmInline
    @SerialName("function_call")
    @Serializable
    public value class FunctionCall(
      @SerialName("call_id")
      public val callId: String,
    ) : Item
  }

  @Serializable
  public data class ItemReference(
    public val type: Type? = null,
    public val id: String,
  ) : InputItem {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("item_reference")
      ItemReference("item_reference"),
      ;
    }
  }

  public object Serializer : KSerializer<InputItem> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.tagged.custom.nested.union.case.InputItem", PolymorphicKind.SEALED) {
      element("Message", Message.serializer().descriptor)
      element("Item", Item.serializer().descriptor)
      element("ItemReference", ItemReference.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): InputItem {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        "function_call" -> return json.decodeFromJsonElement(Item.serializer(), value)
        "item_reference" -> return json.decodeFromJsonElement(ItemReference.serializer(), value)
        "message" -> {
          return json.attemptDeserialize(
            value,
            Message::class to { decodeFromJsonElement(Message.serializer(), it) },
            Item::class to { decodeFromJsonElement(Item.serializer(), it) },
          )
        }
        else -> throw SerializationException("Unknown tag: " + tag + " for io.github.nomisrev.render.test.union.discriminated.tagged.custom.nested.union.case.InputItem")
      }
    }

    override fun serialize(encoder: Encoder, `value`: InputItem) {
      when(value) {
        is Message -> encoder.encodeSerializableValue(Message.serializer(), value)
        is Item -> encoder.encodeSerializableValue(Item.serializer(), value)
        is ItemReference -> encoder.encodeSerializableValue(ItemReference.serializer(), value)
      }
    }
  }
}
