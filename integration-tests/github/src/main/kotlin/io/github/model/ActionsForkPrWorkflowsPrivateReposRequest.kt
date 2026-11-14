package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActionsForkPrWorkflowsPrivateReposRequest(
  @SerialName("run_workflows_from_fork_pull_requests")
  public val runWorkflowsFromForkPullRequests: Boolean,
  @SerialName("send_write_tokens_to_workflows")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sendWriteTokensToWorkflows: Boolean? = null,
  @SerialName("send_secrets_and_variables")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sendSecretsAndVariables: Boolean? = null,
  @SerialName("require_approval_for_fork_pr_workflows")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requireApprovalForForkPrWorkflows: Boolean? = null,
)
