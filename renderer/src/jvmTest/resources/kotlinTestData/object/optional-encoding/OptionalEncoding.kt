package io.github.nomisrev.render.test.object_.optional.encoding

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OptionalEncoding(
  public val requiredNotNullable: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalNotNullable: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalNotNullableWithDefault: Int? = null,
  public val optionalNullable: String? = null,
  public val requiredNullable: String?,
  @Required
  public val requiredNullableWithDefault: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nested: Nested? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val choice: Choice? = null,
) {
  @Serializable(with = Choice.Serializer::class)
  public sealed interface Choice {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class KindAndLabel(
      public val kind: Kind,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val label: String? = null,
    ) : Choice {
      @Serializable
      public enum class Kind(
        public val `value`: String,
      ) {
        @SerialName("a")
        A("a"),
        ;
      }
    }

    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Choice

    public object Serializer : KSerializer<Choice> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.object_.optional.encoding.OptionalEncoding.Choice", PolymorphicKind.SEALED) {
        element("KindAndLabel", KindAndLabel.serializer().descriptor)
        element("CaseString", String.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Choice {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonObject -> json.attemptDeserialize(
            value,
            KindAndLabel::class to { decodeFromJsonElement(KindAndLabel.serializer(), it) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Choice) {
        when(value) {
          is KindAndLabel -> encoder.encodeSerializableValue(KindAndLabel.serializer(), value)
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Nested(
    public val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val note: String? = null,
  )
}
