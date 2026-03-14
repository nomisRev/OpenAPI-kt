package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkflowDispatchResponse(
    @SerialName("workflow_run_id") val workflowRunId: WorkflowRunId,
    @SerialName("run_url") val runUrl: String,
    @SerialName("html_url") val htmlUrl: String,
)
