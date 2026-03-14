package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationSecretScanningAlertResponse(
    val number: AlertNumberResponse? = null,
    @SerialName("created_at") val createdAt: AlertCreatedAtResponse? = null,
    @SerialName("updated_at") val updatedAt: NullableAlertUpdatedAtResponse? = null,
    val url: AlertUrlResponse? = null,
    @SerialName("html_url") val htmlUrl: AlertHtmlUrlResponse? = null,
    @SerialName("locations_url") val locationsUrl: String? = null,
    val state: SecretScanningAlertState? = null,
    val resolution: SecretScanningAlertResolution? = null,
    @SerialName("resolved_at") val resolvedAt: LocalDateTime? = null,
    @SerialName("resolved_by") val resolvedBy: NullableSimpleUser? = null,
    @SerialName("secret_type") val secretType: String? = null,
    @SerialName("secret_type_display_name") val secretTypeDisplayName: String? = null,
    val secret: String? = null,
    val repository: SimpleRepository? = null,
    @SerialName("push_protection_bypassed") val pushProtectionBypassed: Boolean? = null,
    @SerialName("push_protection_bypassed_by") val pushProtectionBypassedBy: NullableSimpleUser? = null,
    @SerialName("push_protection_bypassed_at") val pushProtectionBypassedAt: LocalDateTime? = null,
    @SerialName("push_protection_bypass_request_reviewer") val pushProtectionBypassRequestReviewer: NullableSimpleUser? = null,
    @SerialName("push_protection_bypass_request_reviewer_comment") val pushProtectionBypassRequestReviewerComment: String? = null,
    @SerialName("push_protection_bypass_request_comment") val pushProtectionBypassRequestComment: String? = null,
    @SerialName("push_protection_bypass_request_html_url") val pushProtectionBypassRequestHtmlUrl: String? = null,
    @SerialName("resolution_comment") val resolutionComment: String? = null,
    val validity: Validity? = null,
    @SerialName("publicly_leaked") val publiclyLeaked: Boolean? = null,
    @SerialName("multi_repo") val multiRepo: Boolean? = null,
    @SerialName("is_base64_encoded") val isBase64Encoded: Boolean? = null,
    @SerialName("first_location_detected") val firstLocationDetected: NullableSecretScanningFirstDetectedLocation? = null,
    @SerialName("has_more_locations") val hasMoreLocations: Boolean? = null,
    @SerialName("assigned_to") val assignedTo: NullableSimpleUser? = null,
) {
    @Serializable
    enum class Validity {
        @SerialName("active") Active, @SerialName("inactive") Inactive, @SerialName("unknown") Unknown;
    }
}
