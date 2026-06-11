package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool that must provide code scanning results for this rule to pass.
 */
@Serializable
public data class RepositoryRuleParamsCodeScanningTool(
  @SerialName("alerts_threshold")
  public val alertsThreshold: AlertsThreshold,
  @SerialName("security_alerts_threshold")
  public val securityAlertsThreshold: SecurityAlertsThreshold,
  public val tool: String,
) {
  @Serializable
  public enum class AlertsThreshold(
    public val `value`: String,
  ) {
    @SerialName("none")
    None("none"),
    @SerialName("errors")
    Errors("errors"),
    @SerialName("errors_and_warnings")
    ErrorsAndWarnings("errors_and_warnings"),
    @SerialName("all")
    All("all"),
    ;
  }

  @Serializable
  public enum class SecurityAlertsThreshold(
    public val `value`: String,
  ) {
    @SerialName("none")
    None("none"),
    @SerialName("critical")
    Critical("critical"),
    @SerialName("high_or_higher")
    HighOrHigher("high_or_higher"),
    @SerialName("medium_or_higher")
    MediumOrHigher("medium_or_higher"),
    @SerialName("all")
    All("all"),
    ;
  }
}
