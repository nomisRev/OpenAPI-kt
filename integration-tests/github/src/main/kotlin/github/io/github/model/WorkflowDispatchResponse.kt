package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response containing the workflow run ID and URLs.
 */
@Serializable
public data class WorkflowDispatchResponse(
  @SerialName("workflow_run_id")
  public val workflowRunId: WorkflowRunId,
  @SerialName("run_url")
  public val runUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
)
