package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * An Eval object with a data source config and testing criteria.
 * An Eval represents a task to be done for your LLM integration.
 * Like:
 *  - Improve the quality of my chatbot
 *  - See how well my chatbot handles customer support
 *  - Check if o4-mini is better at my usecase than gpt-4o
 *
 */
@Serializable
public data class Eval(
  @Required
  public val `object`: Object = Object.Eval,
  public val id: String,
  public val name: String,
  @SerialName("data_source_config")
  public val dataSourceConfig: JsonElement,
  @SerialName("testing_criteria")
  public val testingCriteria: List<TestingCriteria>,
  @SerialName("created_at")
  public val createdAt: Long,
  public val metadata: Metadata,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("eval")
    Eval("eval"),
    ;
  }

  @Serializable(with = TestingCriteria.Serializer::class)
  public sealed interface TestingCriteria {
    @Serializable
    @JvmInline
    public value class CaseEvalGraderLabelModel(
      public val `value`: EvalGraderLabelModel,
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
          buildSerialDescriptor("io.openai.model.Eval.TestingCriteria", PolymorphicKind.SEALED) {
        element("CaseEvalGraderLabelModel", EvalGraderLabelModel.serializer().descriptor)
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
          CaseEvalGraderLabelModel::class to { CaseEvalGraderLabelModel(decodeFromJsonElement(EvalGraderLabelModel.serializer(), it)) },
          CaseEvalGraderStringCheck::class to { CaseEvalGraderStringCheck(decodeFromJsonElement(EvalGraderStringCheck.serializer(), it)) },
          CaseEvalGraderTextSimilarity::class to { CaseEvalGraderTextSimilarity(decodeFromJsonElement(EvalGraderTextSimilarity.serializer(), it)) },
          CaseEvalGraderPython::class to { CaseEvalGraderPython(decodeFromJsonElement(EvalGraderPython.serializer(), it)) },
          CaseEvalGraderScoreModel::class to { CaseEvalGraderScoreModel(decodeFromJsonElement(EvalGraderScoreModel.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: TestingCriteria) {
        when(value) {
          is CaseEvalGraderLabelModel -> encoder.encodeSerializableValue(EvalGraderLabelModel.serializer(), value.value)
          is CaseEvalGraderStringCheck -> encoder.encodeSerializableValue(EvalGraderStringCheck.serializer(), value.value)
          is CaseEvalGraderTextSimilarity -> encoder.encodeSerializableValue(EvalGraderTextSimilarity.serializer(), value.value)
          is CaseEvalGraderPython -> encoder.encodeSerializableValue(EvalGraderPython.serializer(), value.value)
          is CaseEvalGraderScoreModel -> encoder.encodeSerializableValue(EvalGraderScoreModel.serializer(), value.value)
        }
      }
    }
  }
}
