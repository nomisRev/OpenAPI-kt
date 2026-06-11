package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
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
  @Serializable
  public data class Item(
    public val id: Long? = null,
    @SerialName("actor_id")
    public val actorId: Long? = null,
    @SerialName("actor_name")
    public val actorName: String? = null,
    @SerialName("before_sha")
    public val beforeSha: String? = null,
    @SerialName("after_sha")
    public val afterSha: String? = null,
    public val ref: String? = null,
    @SerialName("repository_id")
    public val repositoryId: Long? = null,
    @SerialName("repository_name")
    public val repositoryName: String? = null,
    @SerialName("pushed_at")
    public val pushedAt: Instant? = null,
    public val result: Result? = null,
    @SerialName("evaluation_result")
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
