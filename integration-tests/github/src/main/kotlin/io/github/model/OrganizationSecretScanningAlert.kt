package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OrganizationSecretScanningAlert(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val number: AlertNumber? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: AlertCreatedAt? = null,
  @SerialName("updated_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updatedAt: Optional<NullableAlertUpdatedAt> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: AlertUrl? = null,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: AlertHtmlUrl? = null,
  @SerialName("locations_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val locationsUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val state: SecretScanningAlertState? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val resolution: Optional<SecretScanningAlertResolution> = Optional.Absent,
  @SerialName("resolved_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val resolvedAt: Optional<Instant> = Optional.Absent,
  @SerialName("resolved_by")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val resolvedBy: Optional<NullableSimpleUser> = Optional.Absent,
  @SerialName("secret_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretType: String? = null,
  @SerialName("secret_type_display_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretTypeDisplayName: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secret: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repository: SimpleRepository? = null,
  @SerialName("push_protection_bypassed")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pushProtectionBypassed: Optional<Boolean> = Optional.Absent,
  @SerialName("push_protection_bypassed_by")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pushProtectionBypassedBy: Optional<NullableSimpleUser> = Optional.Absent,
  @SerialName("push_protection_bypassed_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pushProtectionBypassedAt: Optional<Instant> = Optional.Absent,
  @SerialName("push_protection_bypass_request_reviewer")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pushProtectionBypassRequestReviewer: Optional<NullableSimpleUser> = Optional.Absent,
  @SerialName("push_protection_bypass_request_reviewer_comment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pushProtectionBypassRequestReviewerComment: Optional<String> = Optional.Absent,
  @SerialName("push_protection_bypass_request_comment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pushProtectionBypassRequestComment: Optional<String> = Optional.Absent,
  @SerialName("push_protection_bypass_request_html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pushProtectionBypassRequestHtmlUrl: Optional<String> = Optional.Absent,
  @SerialName("resolution_comment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val resolutionComment: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val validity: Validity? = null,
  @SerialName("publicly_leaked")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val publiclyLeaked: Optional<Boolean> = Optional.Absent,
  @SerialName("multi_repo")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val multiRepo: Optional<Boolean> = Optional.Absent,
  @SerialName("is_base64_encoded")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isBase64Encoded: Optional<Boolean> = Optional.Absent,
  @SerialName("first_location_detected")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val firstLocationDetected:
      Optional<NullableSecretScanningFirstDetectedLocation> = Optional.Absent,
  @SerialName("has_more_locations")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasMoreLocations: Boolean? = null,
  @SerialName("assigned_to")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignedTo: Optional<NullableSimpleUser> = Optional.Absent,
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
