package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
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

/**
 * The hyperparameters used for the reinforcement fine-tuning job.
 */
@Serializable
public data class FineTuneReinforcementHyperparameters(
  @SerialName("batch_size")
  public val batchSize: BatchSize? = null,
  @SerialName("learning_rate_multiplier")
  public val learningRateMultiplier: LearningRateMultiplier? = null,
  @SerialName("n_epochs")
  public val nEpochs: NEpochs? = null,
  @SerialName("reasoning_effort")
  public val reasoningEffort: ReasoningEffort? = null,
  @SerialName("compute_multiplier")
  public val computeMultiplier: ComputeMultiplier? = null,
  @SerialName("eval_interval")
  public val evalInterval: EvalInterval? = null,
  @SerialName("eval_samples")
  public val evalSamples: EvalSamples? = null,
) {
  /**
   * Number of examples in each batch. A larger batch size means that model parameters are updated less frequently, but with lower variance.
   *
   */
  @Serializable(with = BatchSize.Serializer::class)
  public sealed interface BatchSize {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : BatchSize {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : BatchSize

    public object Serializer : KSerializer<BatchSize> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.FineTuneReinforcementHyperparameters.BatchSize", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): BatchSize {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: BatchSize) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        }
      }
    }
  }

  /**
   * Multiplier on amount of compute used for exploring search space during training.
   *
   */
  @Serializable(with = ComputeMultiplier.Serializer::class)
  public sealed interface ComputeMultiplier {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : ComputeMultiplier {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseDouble(
      public val `value`: Double,
    ) : ComputeMultiplier

    public object Serializer : KSerializer<ComputeMultiplier> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.FineTuneReinforcementHyperparameters.ComputeMultiplier", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("CaseDouble", Double.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): ComputeMultiplier {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: ComputeMultiplier) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
        }
      }
    }
  }

  /**
   * The number of training steps between evaluation runs.
   *
   */
  @Serializable(with = EvalInterval.Serializer::class)
  public sealed interface EvalInterval {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : EvalInterval {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : EvalInterval

    public object Serializer : KSerializer<EvalInterval> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.FineTuneReinforcementHyperparameters.EvalInterval", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): EvalInterval {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: EvalInterval) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        }
      }
    }
  }

  /**
   * Number of evaluation samples to generate per training step.
   *
   */
  @Serializable(with = EvalSamples.Serializer::class)
  public sealed interface EvalSamples {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : EvalSamples {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : EvalSamples

    public object Serializer : KSerializer<EvalSamples> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.FineTuneReinforcementHyperparameters.EvalSamples", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): EvalSamples {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: EvalSamples) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        }
      }
    }
  }

  /**
   * Scaling factor for the learning rate. A smaller learning rate may be useful to avoid overfitting.
   *
   */
  @Serializable(with = LearningRateMultiplier.Serializer::class)
  public sealed interface LearningRateMultiplier {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : LearningRateMultiplier {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseDouble(
      public val `value`: Double,
    ) : LearningRateMultiplier

    public object Serializer : KSerializer<LearningRateMultiplier> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.FineTuneReinforcementHyperparameters.LearningRateMultiplier", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("CaseDouble", Double.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): LearningRateMultiplier {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: LearningRateMultiplier) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
        }
      }
    }
  }

  /**
   * The number of epochs to train the model for. An epoch refers to one full cycle through the training dataset.
   *
   */
  @Serializable(with = NEpochs.Serializer::class)
  public sealed interface NEpochs {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : NEpochs {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : NEpochs

    public object Serializer : KSerializer<NEpochs> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.FineTuneReinforcementHyperparameters.NEpochs", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): NEpochs {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: NEpochs) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class ReasoningEffort(
    public val `value`: String,
  ) {
    @SerialName("default")
    Default("default"),
    @SerialName("low")
    Low("low"),
    @SerialName("medium")
    Medium("medium"),
    @SerialName("high")
    High("high"),
    ;
  }
}
