package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response
 */
@JvmInline
@Serializable
public value class RuleSuites(
  public val items: List<Item>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Item(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @SerialName("actor_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actorId: Long? = null,
    @SerialName("actor_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actorName: String? = null,
    @SerialName("before_sha")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val beforeSha: String? = null,
    @SerialName("after_sha")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val afterSha: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ref: String? = null,
    @SerialName("repository_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val repositoryId: Long? = null,
    @SerialName("repository_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val repositoryName: String? = null,
    @SerialName("pushed_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val pushedAt: Instant? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val result: Result? = null,
    @SerialName("evaluation_result")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val evaluationResult: EvaluationResult? = null,
  ) {
    @Serializable
    public enum class EvaluationResult(
      public val `value`: String,
    ) {
      @SerialName("pass")
      Pass("pass"),
      @SerialName("fail")
      Fail("fail"),
      @SerialName("bypass")
      Bypass("bypass"),
      ;
    }

    @Serializable
    public enum class Result(
      public val `value`: String,
    ) {
      @SerialName("pass")
      Pass("pass"),
      @SerialName("fail")
      Fail("fail"),
      @SerialName("bypass")
      Bypass("bypass"),
      ;
    }
  }
}
