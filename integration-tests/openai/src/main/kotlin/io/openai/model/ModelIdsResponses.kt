package io.openai.model

import kotlin.OptIn
import kotlin.String
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

@Serializable(with = ModelIdsResponses.Serializer::class)
public sealed interface ModelIdsResponses {
  @Serializable
  @JvmInline
  public value class CaseModelIdsShared(
    public val `value`: ModelIdsShared,
  ) : ModelIdsResponses

  @Serializable
  public enum class ResponsesOnlyModel(
    public val `value`: String,
  ) : ModelIdsResponses {
    @SerialName("o1-pro")
    O1Pro("o1-pro"),
    @SerialName("o1-pro-2025-03-19")
    O1Pro20250319("o1-pro-2025-03-19"),
    @SerialName("o3-pro")
    O3Pro("o3-pro"),
    @SerialName("o3-pro-2025-06-10")
    O3Pro20250610("o3-pro-2025-06-10"),
    @SerialName("o3-deep-research")
    O3DeepResearch("o3-deep-research"),
    @SerialName("o3-deep-research-2025-06-26")
    O3DeepResearch20250626("o3-deep-research-2025-06-26"),
    @SerialName("o4-mini-deep-research")
    O4MiniDeepResearch("o4-mini-deep-research"),
    @SerialName("o4-mini-deep-research-2025-06-26")
    O4MiniDeepResearch20250626("o4-mini-deep-research-2025-06-26"),
    @SerialName("computer-use-preview")
    ComputerUsePreview("computer-use-preview"),
    @SerialName("computer-use-preview-2025-03-11")
    ComputerUsePreview20250311("computer-use-preview-2025-03-11"),
    @SerialName("gpt-5-codex")
    Gpt5Codex("gpt-5-codex"),
    @SerialName("gpt-5-pro")
    Gpt5Pro("gpt-5-pro"),
    @SerialName("gpt-5-pro-2025-10-06")
    Gpt5Pro20251006("gpt-5-pro-2025-10-06"),
    @SerialName("gpt-5.1-codex-max")
    Gpt51CodexMax("gpt-5.1-codex-max"),
    ;
  }

  public object Serializer : KSerializer<ModelIdsResponses> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.ModelIdsResponses", PolymorphicKind.SEALED) {
      element("CaseModelIdsShared", ModelIdsShared.serializer().descriptor)
      element("ResponsesOnlyModel", ResponsesOnlyModel.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ModelIdsResponses {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        ResponsesOnlyModel::class to { decodeFromJsonElement(ResponsesOnlyModel.serializer(), it) },
        CaseModelIdsShared::class to { CaseModelIdsShared(decodeFromJsonElement(ModelIdsShared.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: ModelIdsResponses) {
      when(value) {
        is CaseModelIdsShared -> encoder.encodeSerializableValue(ModelIdsShared.serializer(), value.value)
        is ResponsesOnlyModel -> encoder.encodeSerializableValue(ResponsesOnlyModel.serializer(), value)
      }
    }
  }
}
