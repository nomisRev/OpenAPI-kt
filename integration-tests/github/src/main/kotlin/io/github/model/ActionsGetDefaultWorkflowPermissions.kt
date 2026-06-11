package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsGetDefaultWorkflowPermissions(
  @SerialName("default_workflow_permissions")
  public val defaultWorkflowPermissions: ActionsDefaultWorkflowPermissions,
  @SerialName("can_approve_pull_request_reviews")
  public val canApprovePullRequestReviews: ActionsCanApprovePullRequestReviews,
)
