package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response
 */
@Serializable
public data class RuleSuite(
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
  @SerialName("rule_evaluations")
  public val ruleEvaluations: List<RuleEvaluations>? = null,
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

  @Serializable
  public data class RuleEvaluations(
    @SerialName("rule_source")
    public val ruleSource: RuleSource? = null,
    public val enforcement: Enforcement? = null,
    public val result: Result? = null,
    @SerialName("rule_type")
    public val ruleType: String? = null,
    public val details: String? = null,
  ) {
    @Serializable
    public enum class Enforcement(
      public val `value`: String,
    ) {
      @SerialName("active")
      Active("active"),
      @SerialName("evaluate")
      Evaluate("evaluate"),
      @SerialName("deleted ruleset")
      DeletedRuleset("deleted ruleset"),
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
      ;
    }

    @Serializable
    public data class RuleSource(
      public val type: String? = null,
      public val id: Long? = null,
      public val name: String? = null,
    )
  }
}
