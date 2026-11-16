package io.github.nomisrev.render.test.client.allof.union.merge

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Thing(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val code: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val shape: Shape? = null,
) {
  @Serializable(with = Shape.Serializer::class)
  public sealed interface Shape {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class RadiusAndColor(
      public val radius: Double,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val color: String? = null,
    ) : Shape

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class SideAndColor(
      public val side: Double,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val color: String? = null,
    ) : Shape

    public object Serializer : KSerializer<Shape> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.client.allof.union.merge.Thing.Shape", PolymorphicKind.SEALED) {
        element("RadiusAndColor", RadiusAndColor.serializer().descriptor)
        element("SideAndColor", SideAndColor.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Shape {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          RadiusAndColor::class to { decodeFromJsonElement(RadiusAndColor.serializer(), it) },
          SideAndColor::class to { decodeFromJsonElement(SideAndColor.serializer(), it) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Shape) {
        when(value) {
          is RadiusAndColor -> encoder.encodeSerializableValue(RadiusAndColor.serializer(), value)
          is SideAndColor -> encoder.encodeSerializableValue(SideAndColor.serializer(), value)
        }
      }
    }
  }
}
