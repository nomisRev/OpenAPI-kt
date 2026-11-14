package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
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
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

/**
 * The GitHub Pages deployment status.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PageDeployment(
  public val id: Id,
  @SerialName("status_url")
  public val statusUrl: String,
  @SerialName("page_url")
  public val pageUrl: String,
  @SerialName("preview_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val previewUrl: String? = null,
) {
  /**
   * The ID of the GitHub Pages deployment. This is the Git SHA of the deployed commit.
   */
  @Serializable(with = Id.Serializer::class)
  public sealed interface Id {
    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : Id

    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Id

    public object Serializer : KSerializer<Id> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.PageDeployment.Id", PolymorphicKind.SEALED) {
        element("CaseLong", Long.serializer().descriptor)
        element("CaseString", String.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Id {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
            value,
            CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Id) {
        when(value) {
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
      }
    }
  }
}
