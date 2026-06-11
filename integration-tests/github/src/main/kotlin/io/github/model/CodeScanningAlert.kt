package io.github.model

import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningAlert(
  public val number: AlertNumber,
  @SerialName("created_at")
  public val createdAt: AlertCreatedAt,
  @SerialName("updated_at")
  public val updatedAt: AlertUpdatedAt? = null,
  public val url: AlertUrl,
  @SerialName("html_url")
  public val htmlUrl: AlertHtmlUrl,
  @SerialName("instances_url")
  public val instancesUrl: AlertInstancesUrl,
  public val state: CodeScanningAlertState?,
  @SerialName("fixed_at")
  public val fixedAt: AlertFixedAt? = null,
  @SerialName("dismissed_by")
  public val dismissedBy: NullableSimpleUser?,
  @SerialName("dismissed_at")
  public val dismissedAt: AlertDismissedAt?,
  @SerialName("dismissed_reason")
  public val dismissedReason: CodeScanningAlertDismissedReason?,
  @SerialName("dismissed_comment")
  public val dismissedComment: CodeScanningAlertDismissedComment? = null,
  public val rule: CodeScanningAlertRule,
  public val tool: CodeScanningAnalysisTool,
  @SerialName("most_recent_instance")
  public val mostRecentInstance: CodeScanningAlertInstance,
  @SerialName("dismissal_approved_by")
  public val dismissalApprovedBy: NullableSimpleUser? = null,
  public val assignees: List<SimpleUser>? = null,
)
