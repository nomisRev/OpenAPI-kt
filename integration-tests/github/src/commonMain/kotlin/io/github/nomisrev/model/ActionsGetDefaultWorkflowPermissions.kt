package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsGetDefaultWorkflowPermissions(
    @SerialName("default_workflow_permissions") val defaultWorkflowPermissions: ActionsDefaultWorkflowPermissions,
    @SerialName("can_approve_pull_request_reviews") val canApprovePullRequestReviews: ActionsCanApprovePullRequestReviews,
)
