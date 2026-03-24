package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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

@Serializable
public data class CreateEvalRequest(
  public val name: String? = null,
  public val metadata: Metadata? = null,
  @SerialName("data_source_config")
  public val dataSourceConfig: JsonElement,
  @SerialName("testing_criteria")
  public val testingCriteria: List<TestingCriteria>,
) {
  @Serializable(with = TestingCriteria.Serializer::class)
  public sealed interface TestingCriteria {
    @Serializable
    @JvmInline
    public value class CaseCreateEvalLabelModelGrader(
      public val `value`: CreateEvalLabelModelGrader,
    ) : TestingCriteria

    @Serializable
    @JvmInline
    public value class CaseEvalGraderStringCheck(
      public val `value`: EvalGraderStringCheck,
    ) : TestingCriteria

    @Serializable
    @JvmInline
    public value class CaseEvalGraderTextSimilarity(
      public val `value`: EvalGraderTextSimilarity,
    ) : TestingCriteria

    @Serializable
    @JvmInline
    public value class CaseEvalGraderPython(
      public val `value`: EvalGraderPython,
    ) : TestingCriteria

    @Serializable
    @JvmInline
    public value class CaseEvalGraderScoreModel(
      public val `value`: EvalGraderScoreModel,
    ) : TestingCriteria

    public object Serializer : KSerializer<TestingCriteria> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateEvalRequest.TestingCriteria", PolymorphicKind.SEALED) {
        element("CaseCreateEvalLabelModelGrader", CreateEvalLabelModelGrader.serializer().descriptor)
        element("CaseEvalGraderStringCheck", EvalGraderStringCheck.serializer().descriptor)
        element("CaseEvalGraderTextSimilarity", EvalGraderTextSimilarity.serializer().descriptor)
        element("CaseEvalGraderPython", EvalGraderPython.serializer().descriptor)
        element("CaseEvalGraderScoreModel", EvalGraderScoreModel.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): TestingCriteria {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseCreateEvalLabelModelGrader::class to { CaseCreateEvalLabelModelGrader(decodeFromJsonElement(CreateEvalLabelModelGrader.serializer(), it)) },
          CaseEvalGraderStringCheck::class to { CaseEvalGraderStringCheck(decodeFromJsonElement(EvalGraderStringCheck.serializer(), it)) },
          CaseEvalGraderTextSimilarity::class to { CaseEvalGraderTextSimilarity(decodeFromJsonElement(EvalGraderTextSimilarity.serializer(), it)) },
          CaseEvalGraderPython::class to { CaseEvalGraderPython(decodeFromJsonElement(EvalGraderPython.serializer(), it)) },
          CaseEvalGraderScoreModel::class to { CaseEvalGraderScoreModel(decodeFromJsonElement(EvalGraderScoreModel.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: TestingCriteria) {
        when(value) {
          is CaseCreateEvalLabelModelGrader -> encoder.encodeSerializableValue(CreateEvalLabelModelGrader.serializer(), value.value)
          is CaseEvalGraderStringCheck -> encoder.encodeSerializableValue(EvalGraderStringCheck.serializer(), value.value)
          is CaseEvalGraderTextSimilarity -> encoder.encodeSerializableValue(EvalGraderTextSimilarity.serializer(), value.value)
          is CaseEvalGraderPython -> encoder.encodeSerializableValue(EvalGraderPython.serializer(), value.value)
          is CaseEvalGraderScoreModel -> encoder.encodeSerializableValue(EvalGraderScoreModel.serializer(), value.value)
        }
      }
    }
  }
}
