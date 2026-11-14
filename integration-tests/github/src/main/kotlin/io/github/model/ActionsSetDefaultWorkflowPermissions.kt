package io.github.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActionsSetDefaultWorkflowPermissions(
  @SerialName("default_workflow_permissions")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val defaultWorkflowPermissions: ActionsDefaultWorkflowPermissions? = null,
  @SerialName("can_approve_pull_request_reviews")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val canApprovePullRequestReviews: ActionsCanApprovePullRequestReviews? = null,
)
