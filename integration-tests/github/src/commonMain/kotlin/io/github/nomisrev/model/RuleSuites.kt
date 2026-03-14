package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class RuleSuites(val items: List<Item>) {
    @Serializable
    data class Item(
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
    ) {
        @Serializable
        enum class Result {
            @SerialName("pass") Pass, @SerialName("fail") Fail, @SerialName("bypass") Bypass;
        }

        @Serializable
        enum class EvaluationResult {
            @SerialName("pass") Pass, @SerialName("fail") Fail, @SerialName("bypass") Bypass;
        }
    }
}
