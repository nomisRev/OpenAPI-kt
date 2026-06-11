package io.openai.model

import kotlin.ByteArray
import kotlin.OptIn
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Uploads a skill either as a directory (multipart `files[]`) or as a single zip file.
 */
@JvmInline
@Serializable
public value class CreateSkillBody(
  public val files: Files,
) {
  @Serializable(with = Files.Serializer::class)
  public sealed interface Files {
    @Serializable
    @JvmInline
    public value class CaseBinaries(
      public val `value`: List<ByteArray>,
    ) : Files

    @Serializable
    @JvmInline
    public value class CaseBinary(
      public val `value`: ByteArray,
    ) : Files

    public object Serializer : KSerializer<Files> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateSkillBody.Files", PolymorphicKind.SEALED) {
        element("CaseBinaries", ListSerializer(ByteArraySerializer()).descriptor)
        element("CaseBinary", ByteArraySerializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Files {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseBinaries::class to { CaseBinaries(decodeFromJsonElement(ListSerializer(ByteArraySerializer()), it)) },
          CaseBinary::class to { CaseBinary(decodeFromJsonElement(ByteArraySerializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Files) {
        when(value) {
          is CaseBinaries -> encoder.encodeSerializableValue(ListSerializer(ByteArraySerializer()), value.value)
          is CaseBinary -> encoder.encodeSerializableValue(ByteArraySerializer(), value.value)
        }
      }
    }
  }
}
