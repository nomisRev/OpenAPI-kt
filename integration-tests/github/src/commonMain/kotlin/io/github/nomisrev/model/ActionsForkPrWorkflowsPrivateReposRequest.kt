package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsForkPrWorkflowsPrivateReposRequest(
    @SerialName("run_workflows_from_fork_pull_requests") val runWorkflowsFromForkPullRequests: Boolean,
    @SerialName("send_write_tokens_to_workflows") val sendWriteTokensToWorkflows: Boolean? = null,
    @SerialName("send_secrets_and_variables") val sendSecretsAndVariables: Boolean? = null,
    @SerialName("require_approval_for_fork_pr_workflows") val requireApprovalForForkPrWorkflows: Boolean? = null,
)
