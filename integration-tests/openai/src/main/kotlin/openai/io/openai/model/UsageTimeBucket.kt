package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
    @Serializable
    public data class OrganizationUsageCompletionsResult(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("input_cached_tokens")
      public val inputCachedTokens: Long? = null,
      @SerialName("output_tokens")
      public val outputTokens: Long,
      @SerialName("input_audio_tokens")
      public val inputAudioTokens: Long? = null,
      @SerialName("output_audio_tokens")
      public val outputAudioTokens: Long? = null,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      public val projectId: String? = null,
      @SerialName("user_id")
      public val userId: String? = null,
      @SerialName("api_key_id")
      public val apiKeyId: String? = null,
      public val model: String? = null,
      public val batch: Boolean? = null,
      @SerialName("service_tier")
      public val serviceTier: String? = null,
    ) : Result

    /**
     * The aggregated embeddings usage details of the specific time bucket.
     */
    @SerialName("organization.usage.embeddings.result")
    @Serializable
    public data class OrganizationUsageEmbeddingsResult(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      public val projectId: String? = null,
      @SerialName("user_id")
      public val userId: String? = null,
      @SerialName("api_key_id")
      public val apiKeyId: String? = null,
      public val model: String? = null,
    ) : Result

    /**
     * The aggregated moderations usage details of the specific time bucket.
     */
    @SerialName("organization.usage.moderations.result")
    @Serializable
    public data class OrganizationUsageModerationsResult(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      public val projectId: String? = null,
      @SerialName("user_id")
      public val userId: String? = null,
      @SerialName("api_key_id")
      public val apiKeyId: String? = null,
      public val model: String? = null,
    ) : Result

    /**
     * The aggregated images usage details of the specific time bucket.
     */
    @SerialName("organization.usage.images.result")
    @Serializable
    public data class OrganizationUsageImagesResult(
      public val images: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      public val source: String? = null,
      public val size: String? = null,
      @SerialName("project_id")
      public val projectId: String? = null,
      @SerialName("user_id")
      public val userId: String? = null,
      @SerialName("api_key_id")
      public val apiKeyId: String? = null,
      public val model: String? = null,
    ) : Result

    /**
     * The aggregated audio speeches usage details of the specific time bucket.
     */
    @SerialName("organization.usage.audio_speeches.result")
    @Serializable
    public data class OrganizationUsageAudioSpeechesResult(
      public val characters: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      public val projectId: String? = null,
      @SerialName("user_id")
      public val userId: String? = null,
      @SerialName("api_key_id")
      public val apiKeyId: String? = null,
      public val model: String? = null,
    ) : Result

    /**
     * The aggregated audio transcriptions usage details of the specific time bucket.
     */
    @SerialName("organization.usage.audio_transcriptions.result")
    @Serializable
    public data class OrganizationUsageAudioTranscriptionsResult(
      public val seconds: Long,
      @SerialName("num_model_requests")
      public val numModelRequests: Long,
      @SerialName("project_id")
      public val projectId: String? = null,
      @SerialName("user_id")
      public val userId: String? = null,
      @SerialName("api_key_id")
      public val apiKeyId: String? = null,
      public val model: String? = null,
    ) : Result

    /**
     * The aggregated vector stores usage details of the specific time bucket.
     */
    @SerialName("organization.usage.vector_stores.result")
    @Serializable
    public data class OrganizationUsageVectorStoresResult(
      @SerialName("usage_bytes")
      public val usageBytes: Long,
      @SerialName("project_id")
      public val projectId: String? = null,
    ) : Result

    /**
     * The aggregated code interpreter sessions usage details of the specific time bucket.
     */
    @SerialName("organization.usage.code_interpreter_sessions.result")
    @Serializable
    public data class OrganizationUsageCodeInterpreterSessionsResult(
      @SerialName("num_sessions")
      public val numSessions: Long? = null,
      @SerialName("project_id")
      public val projectId: String? = null,
    ) : Result

    /**
     * The aggregated costs details of the specific time bucket.
     */
    @SerialName("organization.costs.result")
    @Serializable
    public data class OrganizationCostsResult(
      public val amount: Amount? = null,
      @SerialName("line_item")
      public val lineItem: String? = null,
      @SerialName("project_id")
      public val projectId: String? = null,
    ) : Result {
      /**
       * The monetary value in its associated currency.
       */
      @Serializable
      public data class Amount(
        public val `value`: Double? = null,
        public val currency: String? = null,
      )
    }
  }
}
