package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsSetDefaultWorkflowPermissions(
    @SerialName("default_workflow_permissions") val defaultWorkflowPermissions: ActionsDefaultWorkflowPermissions? = null,
    @SerialName("can_approve_pull_request_reviews") val canApprovePullRequestReviews: ActionsCanApprovePullRequestReviews? = null,
)
