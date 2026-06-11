package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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

@Serializable
public data class CreateFineTuningJobRequest(
  public val model: Model,
  @SerialName("training_file")
  public val trainingFile: String,
  public val hyperparameters: Hyperparameters? = null,
  public val suffix: String? = null,
  @SerialName("validation_file")
  public val validationFile: String? = null,
  public val integrations: List<Integrations>? = null,
  public val seed: Long? = null,
  public val method: FineTuneMethod? = null,
  public val metadata: Metadata? = null,
) {
  /**
   * The hyperparameters used for the fine-tuning job.
   * This value is now deprecated in favor of `method`, and should be passed in under the `method` parameter.
   *
   */
  @Serializable
  public data class Hyperparameters(
    @SerialName("batch_size")
    public val batchSize: BatchSize? = null,
    @SerialName("learning_rate_multiplier")
    public val learningRateMultiplier: LearningRateMultiplier? = null,
    @SerialName("n_epochs")
    public val nEpochs: NEpochs? = null,
  ) {
    /**
     * Number of examples in each batch. A larger batch size means that model parameters
     * are updated less frequently, but with lower variance.
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
            buildSerialDescriptor("io.openai.model.CreateFineTuningJobRequest.Hyperparameters.BatchSize", PolymorphicKind.SEALED) {
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
     * Scaling factor for the learning rate. A smaller learning rate may be useful to avoid
     * overfitting.
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
            buildSerialDescriptor("io.openai.model.CreateFineTuningJobRequest.Hyperparameters.LearningRateMultiplier", PolymorphicKind.SEALED) {
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
     * The number of epochs to train the model for. An epoch refers to one full cycle
     * through the training dataset.
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
            buildSerialDescriptor("io.openai.model.CreateFineTuningJobRequest.Hyperparameters.NEpochs", PolymorphicKind.SEALED) {
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
  }

  @Serializable
  public data class Integrations(
    public val type: Type,
    public val wandb: Wandb,
  ) {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("wandb")
      Wandb("wandb"),
      ;
    }

    /**
     * The settings for your integration with Weights and Biases. This payload specifies the project that
     * metrics will be sent to. Optionally, you can set an explicit display name for your run, add tags
     * to your run, and set a default entity (team, username, etc) to be associated with your run.
     *
     */
    @Serializable
    public data class Wandb(
      public val project: String,
      public val name: String? = null,
      public val entity: String? = null,
      public val tags: List<String>? = null,
    )
  }

  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini(
      override val `value`: String,
    ) : Model {
      @SerialName("babbage-002")
      Babbage002("babbage-002"),
      @SerialName("davinci-002")
      Davinci002("davinci-002"),
      @SerialName("gpt-3.5-turbo")
      Gpt35Turbo("gpt-3.5-turbo"),
      @SerialName("gpt-4o-mini")
      Gpt4oMini("gpt-4o-mini"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Babbage002 -> encoder.encodeString("babbage-002")
          Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Davinci002 -> encoder.encodeString("davinci-002")
          Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Gpt35Turbo -> encoder.encodeString("gpt-3.5-turbo")
          Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Gpt4oMini -> encoder.encodeString("gpt-4o-mini")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "babbage-002" -> Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Babbage002
        "davinci-002" -> Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Davinci002
        "gpt-3.5-turbo" -> Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Gpt35Turbo
        "gpt-4o-mini" -> Babbage002OrDavinci002OrGpt35TurboOrGpt4oMini.Gpt4oMini
        else -> CaseString(value)
      }
    }
  }
}
