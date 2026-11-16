package io.github.nomisrev.render.test.client.allof.additional.properties

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Thing(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val uri: Uri? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val level: Level? = null,
) {
  @Serializable(with = Uri.Serializer::class)
  public sealed interface Uri {
    @Serializable
    @JvmInline
    public value class CaseIcon(
      public val `value`: Icon,
    ) : Uri

    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Uri

    public object Serializer : KSerializer<Uri> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.client.allof.additional.properties.Thing.Uri", PolymorphicKind.SEALED) {
        element("CaseIcon", Icon.serializer().descriptor)
        element("CaseString", String.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Uri {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseIcon::class to { CaseIcon(decodeFromJsonElement(Icon.serializer(), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Uri) {
        when(value) {
          is CaseIcon -> encoder.encodeSerializableValue(Icon.serializer(), value.value)
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
      }
    }
  }
}
