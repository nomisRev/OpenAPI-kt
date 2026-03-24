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

/**
 * The `fine_tuning.job` object represents a fine-tuning job that has been created through the API.
 *
 */
@Serializable
public data class FineTuningJob(
  public val id: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val error: Error?,
  @SerialName("fine_tuned_model")
  public val fineTunedModel: String?,
  @SerialName("finished_at")
  public val finishedAt: Long?,
  public val hyperparameters: Hyperparameters,
  public val model: String,
  public val `object`: Object,
  @SerialName("organization_id")
  public val organizationId: String,
  @SerialName("result_files")
  public val resultFiles: List<String>,
  public val status: Status,
  @SerialName("trained_tokens")
  public val trainedTokens: Long?,
  @SerialName("training_file")
  public val trainingFile: String,
  @SerialName("validation_file")
  public val validationFile: String?,
  public val integrations: List<FineTuningIntegration>? = null,
  public val seed: Long,
  @SerialName("estimated_finish")
  public val estimatedFinish: Long? = null,
  public val method: FineTuneMethod? = null,
  public val metadata: Metadata? = null,
) {
  /**
   * For fine-tuning jobs that have `failed`, this will contain more information on the cause of the failure.
   */
  @Serializable
  public data class Error(
    public val code: String,
    public val message: String,
    public val `param`: String?,
  )

  /**
   * The hyperparameters used for the fine-tuning job. This value will only be returned when running `supervised` jobs.
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
            buildSerialDescriptor("io.openai.model.FineTuningJob.Hyperparameters.BatchSize", PolymorphicKind.SEALED) {
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
            buildSerialDescriptor("io.openai.model.FineTuningJob.Hyperparameters.LearningRateMultiplier", PolymorphicKind.SEALED) {
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
            buildSerialDescriptor("io.openai.model.FineTuningJob.Hyperparameters.NEpochs", PolymorphicKind.SEALED) {
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
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("fine_tuning.job")
    FineTuningJob("fine_tuning.job"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("validating_files")
    ValidatingFiles("validating_files"),
    @SerialName("queued")
    Queued("queued"),
    @SerialName("running")
    Running("running"),
    @SerialName("succeeded")
    Succeeded("succeeded"),
    @SerialName("failed")
    Failed("failed"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    ;
  }
}
