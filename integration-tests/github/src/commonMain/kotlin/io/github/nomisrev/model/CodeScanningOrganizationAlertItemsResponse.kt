package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningOrganizationAlertItemsResponse(
    val number: AlertNumberResponse,
    @SerialName("created_at") val createdAt: AlertCreatedAtResponse,
    @SerialName("updated_at") val updatedAt: AlertUpdatedAtResponse? = null,
    val url: AlertUrlResponse,
    @SerialName("html_url") val htmlUrl: AlertHtmlUrlResponse,
    @SerialName("instances_url") val instancesUrl: AlertInstancesUrlResponse,
    val state: CodeScanningAlertState?,
    @SerialName("fixed_at") val fixedAt: AlertFixedAtResponse? = null,
    @SerialName("dismissed_by") val dismissedBy: NullableSimpleUser?,
    @SerialName("dismissed_at") val dismissedAt: AlertDismissedAtResponse?,
    @SerialName("dismissed_reason") val dismissedReason: CodeScanningAlertDismissedReason?,
    @SerialName("dismissed_comment") val dismissedComment: CodeScanningAlertDismissedComment? = null,
    val rule: CodeScanningAlertRuleSummary,
    val tool: CodeScanningAnalysisTool,
    @SerialName("most_recent_instance") val mostRecentInstance: CodeScanningAlertInstance,
    val repository: SimpleRepository,
    @SerialName("dismissal_approved_by") val dismissalApprovedBy: NullableSimpleUser? = null,
    val assignees: List<SimpleUser>? = null,
)
