package io.github.nomisrev.render.test.union.discriminated.tagged.custom.collision

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

@Serializable(with = Node.Serializer::class)
public sealed interface Node {
  @Serializable
  public data class TypeAndRemoteId(
    public val type: Type,
    public val remoteId: String,
  ) : Node {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("node")
      Node("node"),
      @SerialName("remote")
      Remote("remote"),
      ;
    }
  }

  @Serializable
  public data class TypeAndPath(
    public val type: Type,
    public val path: String,
  ) : Node {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("node")
      Node("node"),
      @SerialName("local")
      Local("local"),
      ;
    }
  }

  @Serializable
  public data class Leaf(
    public val type: Type,
    public val name: String,
  ) : Node {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("leaf")
      Leaf("leaf"),
      ;
    }
  }

  public object Serializer : KSerializer<Node> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.tagged.custom.collision.Node", PolymorphicKind.SEALED) {
      element("TypeAndRemoteId", TypeAndRemoteId.serializer().descriptor)
      element("TypeAndPath", TypeAndPath.serializer().descriptor)
      element("Leaf", Leaf.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Node {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        "remote" -> return json.decodeFromJsonElement(TypeAndRemoteId.serializer(), value)
        "local" -> return json.decodeFromJsonElement(TypeAndPath.serializer(), value)
        "leaf" -> return json.decodeFromJsonElement(Leaf.serializer(), value)
        "node" -> {
          val keys = obj?.keys.orEmpty()
          if ("remoteId" in keys) {
            return json.decodeFromJsonElement(TypeAndRemoteId.serializer(), value)
          }
          if ("path" in keys) {
            return json.decodeFromJsonElement(TypeAndPath.serializer(), value)
          }
          return json.attemptDeserialize(
            value,
            TypeAndRemoteId::class to { decodeFromJsonElement(TypeAndRemoteId.serializer(), it) },
            TypeAndPath::class to { decodeFromJsonElement(TypeAndPath.serializer(), it) },
          )
        }
        else -> throw SerializationException("Unknown tag: " + tag + " for io.github.nomisrev.render.test.union.discriminated.tagged.custom.collision.Node")
      }
    }

    override fun serialize(encoder: Encoder, `value`: Node) {
      when(value) {
        is TypeAndRemoteId -> encoder.encodeSerializableValue(TypeAndRemoteId.serializer(), value)
        is TypeAndPath -> encoder.encodeSerializableValue(TypeAndPath.serializer(), value)
        is Leaf -> encoder.encodeSerializableValue(Leaf.serializer(), value)
      }
    }
  }
}
