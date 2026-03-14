package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ActionsForkPrContributorApproval(@SerialName("approval_policy") val approvalPolicy: ApprovalPolicy) {
    @Serializable
    enum class ApprovalPolicy {
        @SerialName("first_time_contributors_new_to_github")
        FirstTimeContributorsNewToGithub,
        @SerialName("first_time_contributors")
        FirstTimeContributors,
        @SerialName("all_external_contributors")
        AllExternalContributors;
    }
}
