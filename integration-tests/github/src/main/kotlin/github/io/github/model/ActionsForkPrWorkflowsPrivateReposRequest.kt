package io.github.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsForkPrWorkflowsPrivateReposRequest(
  @SerialName("run_workflows_from_fork_pull_requests")
  public val runWorkflowsFromForkPullRequests: Boolean,
  @SerialName("send_write_tokens_to_workflows")
  public val sendWriteTokensToWorkflows: Boolean? = null,
  @SerialName("send_secrets_and_variables")
  public val sendSecretsAndVariables: Boolean? = null,
  @SerialName("require_approval_for_fork_pr_workflows")
  public val requireApprovalForForkPrWorkflows: Boolean? = null,
)
