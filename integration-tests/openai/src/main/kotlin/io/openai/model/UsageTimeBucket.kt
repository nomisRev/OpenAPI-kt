package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
public data class UsageTimeBucket(
  public val `object`: Object,
  @SerialName("start_time")
  public val startTime: Long,
  @SerialName("end_time")
  public val endTime: Long,
  public val result: List<Result>,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("bucket")
    Bucket("bucket"),
    ;
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("object")
  @Serializable
  public sealed interface Result {
    /**
     * The aggregated completions usage details of the specific time bucket.
     */
    @SerialName("organization.usage.completions.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageCompletionsResult(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("input_cached_tokens")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val inputCachedTokens: Long? = null,
      @SerialName("output_tokens")
      public val outputTokens: Long,
      @SerialName("input_audio_tokens")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val inputAudioTokens: Long? = null,
      @SerialName("output_audio_tokens")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val outputAudioTokens: Long? = null,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
      @SerialName("user_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val userId: Optional<String> = Optional.Absent,
      @SerialName("api_key_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiKeyId: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val model: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val batch: Optional<Boolean> = Optional.Absent,
      @SerialName("service_tier")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val serviceTier: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated embeddings usage details of the specific time bucket.
     */
    @SerialName("organization.usage.embeddings.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageEmbeddingsResult(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
      @SerialName("user_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val userId: Optional<String> = Optional.Absent,
      @SerialName("api_key_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiKeyId: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val model: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated moderations usage details of the specific time bucket.
     */
    @SerialName("organization.usage.moderations.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageModerationsResult(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
      @SerialName("user_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val userId: Optional<String> = Optional.Absent,
      @SerialName("api_key_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiKeyId: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val model: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated images usage details of the specific time bucket.
     */
    @SerialName("organization.usage.images.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageImagesResult(
      public val images: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val source: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val size: Optional<String> = Optional.Absent,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
      @SerialName("user_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val userId: Optional<String> = Optional.Absent,
      @SerialName("api_key_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiKeyId: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val model: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated audio speeches usage details of the specific time bucket.
     */
    @SerialName("organization.usage.audio_speeches.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageAudioSpeechesResult(
      public val characters: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
      @SerialName("user_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val userId: Optional<String> = Optional.Absent,
      @SerialName("api_key_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiKeyId: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val model: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated audio transcriptions usage details of the specific time bucket.
     */
    @SerialName("organization.usage.audio_transcriptions.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageAudioTranscriptionsResult(
      public val seconds: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
      @SerialName("user_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val userId: Optional<String> = Optional.Absent,
      @SerialName("api_key_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiKeyId: Optional<String> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val model: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated vector stores usage details of the specific time bucket.
     */
    @SerialName("organization.usage.vector_stores.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageVectorStoresResult(
      @SerialName("usage_bytes")
      public val usageBytes: Long,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated code interpreter sessions usage details of the specific time bucket.
     */
    @SerialName("organization.usage.code_interpreter_sessions.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationUsageCodeInterpreterSessionsResult(
      @SerialName("num_sessions")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val numSessions: Long? = null,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
    ) : Result

    /**
     * The aggregated costs details of the specific time bucket.
     */
    @SerialName("organization.costs.result")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class OrganizationCostsResult(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val amount: Amount? = null,
      @SerialName("line_item")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val lineItem: Optional<String> = Optional.Absent,
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: Optional<String> = Optional.Absent,
    ) : Result {
      /**
       * The monetary value in its associated currency.
       */
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Amount(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val `value`: Double? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val currency: String? = null,
      )
    }
  }
}
