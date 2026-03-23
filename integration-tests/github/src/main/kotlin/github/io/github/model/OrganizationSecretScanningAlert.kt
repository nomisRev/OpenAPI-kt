package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class OrganizationSecretScanningAlert(
  public val number: AlertNumberWrite? = null,
  @SerialName("created_at")
  public val createdAt: AlertCreatedAtWrite? = null,
  @SerialName("updated_at")
  public val updatedAt: NullableAlertUpdatedAt? = null,
  public val url: AlertUrlWrite? = null,
  @SerialName("html_url")
  public val htmlUrl: AlertHtmlUrlWrite? = null,
  @SerialName("locations_url")
  public val locationsUrl: String? = null,
  public val state: SecretScanningAlertState? = null,
  public val resolution: SecretScanningAlertResolution? = null,
  @SerialName("resolved_at")
  public val resolvedAt: Instant? = null,
  @SerialName("resolved_by")
  public val resolvedBy: NullableSimpleUser? = null,
  @SerialName("secret_type")
  public val secretType: String? = null,
  @SerialName("secret_type_display_name")
  public val secretTypeDisplayName: String? = null,
  public val secret: String? = null,
  public val repository: SimpleRepository? = null,
  @SerialName("push_protection_bypassed")
  public val pushProtectionBypassed: Boolean? = null,
  @SerialName("push_protection_bypassed_by")
  public val pushProtectionBypassedBy: NullableSimpleUser? = null,
  @SerialName("push_protection_bypassed_at")
  public val pushProtectionBypassedAt: Instant? = null,
  @SerialName("push_protection_bypass_request_reviewer")
  public val pushProtectionBypassRequestReviewer: NullableSimpleUser? = null,
  @SerialName("push_protection_bypass_request_reviewer_comment")
  public val pushProtectionBypassRequestReviewerComment: String? = null,
  @SerialName("push_protection_bypass_request_comment")
  public val pushProtectionBypassRequestComment: String? = null,
  @SerialName("push_protection_bypass_request_html_url")
  public val pushProtectionBypassRequestHtmlUrl: String? = null,
  @SerialName("resolution_comment")
  public val resolutionComment: String? = null,
  public val validity: Validity? = null,
  @SerialName("publicly_leaked")
  public val publiclyLeaked: Boolean? = null,
  @SerialName("multi_repo")
  public val multiRepo: Boolean? = null,
  @SerialName("is_base64_encoded")
  public val isBase64Encoded: Boolean? = null,
  @SerialName("first_location_detected")
  public val firstLocationDetected: NullableSecretScanningFirstDetectedLocation? = null,
  @SerialName("has_more_locations")
  public val hasMoreLocations: Boolean? = null,
  @SerialName("assigned_to")
  public val assignedTo: NullableSimpleUser? = null,
) {
  @Serializable
  public enum class Validity(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    @SerialName("inactive")
    Inactive("inactive"),
    @SerialName("unknown")
    Unknown("unknown"),
    ;
  }
}
