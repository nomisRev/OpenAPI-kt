package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsSetDefaultWorkflowPermissions(
  @SerialName("default_workflow_permissions")
  public val defaultWorkflowPermissions: ActionsDefaultWorkflowPermissions? = null,
  @SerialName("can_approve_pull_request_reviews")
  public val canApprovePullRequestReviews: ActionsCanApprovePullRequestReviews? = null,
)
