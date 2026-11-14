package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Dependabot alert.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DependabotAlertWithRepository(
  public val number: AlertNumber,
  public val state: State,
  public val dependency: Dependency,
  @SerialName("security_advisory")
  public val securityAdvisory: DependabotAlertSecurityAdvisory,
  @SerialName("security_vulnerability")
  public val securityVulnerability: DependabotAlertSecurityVulnerability,
  public val url: AlertUrl,
  @SerialName("html_url")
  public val htmlUrl: AlertHtmlUrl,
  @SerialName("created_at")
  public val createdAt: AlertCreatedAt,
  @SerialName("updated_at")
  public val updatedAt: AlertUpdatedAt,
  @SerialName("dismissed_at")
  public val dismissedAt: AlertDismissedAt?,
  @SerialName("dismissed_by")
  public val dismissedBy: NullableSimpleUser?,
  @SerialName("dismissed_reason")
  public val dismissedReason: DismissedReason?,
  @SerialName("dismissed_comment")
  public val dismissedComment: String?,
  @SerialName("fixed_at")
  public val fixedAt: AlertFixedAt?,
  @SerialName("auto_dismissed_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val autoDismissedAt: Optional<AlertAutoDismissedAt> = Optional.Absent,
  @SerialName("dismissal_request")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dismissalRequest: Optional<DependabotAlertDismissalRequestSimple> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignees: List<SimpleUser>? = null,
  public val repository: SimpleRepository,
) {
  /**
   * Details for the vulnerable dependency.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Dependency(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `package`: DependabotAlertPackage? = null,
    @SerialName("manifest_path")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val manifestPath: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val scope: Optional<Scope> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val relationship: Optional<Relationship> = Optional.Absent,
  ) {
    @Serializable
    public enum class Relationship(
      public val `value`: String,
    ) {
      @SerialName("unknown")
      Unknown("unknown"),
      @SerialName("direct")
      Direct("direct"),
      @SerialName("transitive")
      Transitive("transitive"),
      ;
    }

    @Serializable
    public enum class Scope(
      public val `value`: String,
    ) {
      @SerialName("development")
      Development("development"),
      @SerialName("runtime")
      Runtime("runtime"),
      ;
    }
  }

  @Serializable
  public enum class DismissedReason(
    public val `value`: String,
  ) {
    @SerialName("fix_started")
    FixStarted("fix_started"),
    @SerialName("inaccurate")
    Inaccurate("inaccurate"),
    @SerialName("no_bandwidth")
    NoBandwidth("no_bandwidth"),
    @SerialName("not_used")
    NotUsed("not_used"),
    @SerialName("tolerable_risk")
    TolerableRisk("tolerable_risk"),
    ;
  }

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("auto_dismissed")
    AutoDismissed("auto_dismissed"),
    @SerialName("dismissed")
    Dismissed("dismissed"),
    @SerialName("fixed")
    Fixed("fixed"),
    @SerialName("open")
    Open("open"),
    ;
  }
}
