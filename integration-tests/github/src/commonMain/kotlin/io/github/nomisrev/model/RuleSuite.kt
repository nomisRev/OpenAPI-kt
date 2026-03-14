package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RuleSuite(
    val id: Long? = null,
    @SerialName("actor_id") val actorId: Long? = null,
    @SerialName("actor_name") val actorName: String? = null,
    @SerialName("before_sha") val beforeSha: String? = null,
    @SerialName("after_sha") val afterSha: String? = null,
    val ref: String? = null,
    @SerialName("repository_id") val repositoryId: Long? = null,
    @SerialName("repository_name") val repositoryName: String? = null,
    @SerialName("pushed_at") val pushedAt: LocalDateTime? = null,
    val result: Result? = null,
    @SerialName("evaluation_result") val evaluationResult: EvaluationResult? = null,
    @SerialName("rule_evaluations") val ruleEvaluations: List<RuleEvaluations>? = null,
) {
    @Serializable
    enum class Result {
        @SerialName("pass") Pass, @SerialName("fail") Fail, @SerialName("bypass") Bypass;
    }

    @Serializable
    enum class EvaluationResult {
        @SerialName("pass") Pass, @SerialName("fail") Fail, @SerialName("bypass") Bypass;
    }

    @Serializable
    data class RuleEvaluations(
        @SerialName("rule_source") val ruleSource: RuleSource? = null,
        val enforcement: Enforcement? = null,
        val result: Result? = null,
        @SerialName("rule_type") val ruleType: String? = null,
        val details: String? = null,
    ) {
        @Serializable
        data class RuleSource(val type: String? = null, val id: Long? = null, val name: String? = null)

        @Serializable
        enum class Enforcement {
            @SerialName("active") Active, @SerialName("evaluate") Evaluate, @SerialName("deleted ruleset") DeletedRuleset;
        }

        @Serializable
        enum class Result {
            @SerialName("pass") Pass, @SerialName("fail") Fail;
        }
    }
}
