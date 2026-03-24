package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
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

/**
 * Model ID used to generate the response, like `gpt-5` or `o3`. OpenAI offers a wide range of models with different capabilities, performance characteristics, and price points. Refer to the [model guide](/docs/models) to browse and compare available models.
 */
@Serializable(with = ModelIdsCompaction.Serializer::class)
public sealed interface ModelIdsCompaction {
  @Serializable
  @JvmInline
  public value class CaseModelIdsResponses(
    public val `value`: ModelIdsResponses,
  ) : ModelIdsCompaction

  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : ModelIdsCompaction

  public object Serializer : KSerializer<ModelIdsCompaction> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.ModelIdsCompaction", PolymorphicKind.SEALED) {
      element("CaseModelIdsResponses", ModelIdsResponses.serializer().descriptor)
      element("CaseString", String.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ModelIdsCompaction {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseModelIdsResponses::class to { CaseModelIdsResponses(decodeFromJsonElement(ModelIdsResponses.serializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: ModelIdsCompaction) {
      when(value) {
        is CaseModelIdsResponses -> encoder.encodeSerializableValue(ModelIdsResponses.serializer(), value.value)
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
      }
    }
  }
}
