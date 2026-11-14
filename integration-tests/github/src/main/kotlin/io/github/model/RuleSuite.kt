package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RuleSuite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @SerialName("actor_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val actorId: Optional<Long> = Optional.Absent,
  @SerialName("actor_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val actorName: Optional<String> = Optional.Absent,
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
  public val evaluationResult: Optional<EvaluationResult> = Optional.Absent,
  @SerialName("rule_evaluations")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RuleEvaluations(
    @SerialName("rule_source")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ruleSource: RuleSource? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enforcement: Enforcement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val result: Result? = null,
    @SerialName("rule_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ruleType: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val details: Optional<String> = Optional.Absent,
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

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class RuleSource(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val type: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: Optional<Long> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: Optional<String> = Optional.Absent,
    )
  }
}
