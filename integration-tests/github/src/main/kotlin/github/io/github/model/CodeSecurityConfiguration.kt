package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A code security configuration
 */
@Serializable
public data class CodeSecurityConfiguration(
  public val id: Long? = null,
  public val name: String? = null,
  @SerialName("target_type")
  public val targetType: TargetType? = null,
  public val description: String? = null,
  @SerialName("advanced_security")
  public val advancedSecurity: AdvancedSecurity? = null,
  @SerialName("dependency_graph")
  public val dependencyGraph: DependencyGraph? = null,
  @SerialName("dependency_graph_autosubmit_action")
  public val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
  @SerialName("dependency_graph_autosubmit_action_options")
  public val dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
  @SerialName("dependabot_alerts")
  public val dependabotAlerts: DependabotAlerts? = null,
  @SerialName("dependabot_security_updates")
  public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
  @SerialName("dependabot_delegated_alert_dismissal")
  public val dependabotDelegatedAlertDismissal: DependabotDelegatedAlertDismissal? = null,
  @SerialName("code_scanning_options")
  public val codeScanningOptions: CodeScanningOptions? = null,
  @SerialName("code_scanning_default_setup")
  public val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
  @SerialName("code_scanning_default_setup_options")
  public val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
  @SerialName("code_scanning_delegated_alert_dismissal")
  public val codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
  @SerialName("secret_scanning")
  public val secretScanning: SecretScanning? = null,
  @SerialName("secret_scanning_push_protection")
  public val secretScanningPushProtection: SecretScanningPushProtection? = null,
  @SerialName("secret_scanning_delegated_bypass")
  public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
  @SerialName("secret_scanning_delegated_bypass_options")
  public val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
  @SerialName("secret_scanning_validity_checks")
  public val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
  @SerialName("secret_scanning_non_provider_patterns")
  public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
  @SerialName("secret_scanning_generic_secrets")
  public val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
  @SerialName("secret_scanning_delegated_alert_dismissal")
  public val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
  @SerialName("secret_scanning_extended_metadata")
  public val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
  @SerialName("private_vulnerability_reporting")
  public val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
  public val enforcement: Enforcement? = null,
  public val url: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String? = null,
  @SerialName("created_at")
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
) {
  @Serializable
  public enum class AdvancedSecurity(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("code_security")
    CodeSecurity("code_security"),
    @SerialName("secret_protection")
    SecretProtection("secret_protection"),
    ;
  }

  @Serializable
  public enum class CodeScanningDefaultSetup(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  /**
   * Feature options for code scanning default setup
   */
  @Serializable
  public data class CodeScanningDefaultSetupOptions(
    @SerialName("runner_type")
    public val runnerType: RunnerType? = null,
    @SerialName("runner_label")
    public val runnerLabel: String? = null,
  ) {
    @Serializable
    public enum class RunnerType(
      public val `value`: String,
    ) {
      @SerialName("standard")
      Standard("standard"),
      @SerialName("labeled")
      Labeled("labeled"),
      @SerialName("not_set")
      NotSet("not_set"),
      ;
    }
  }

  @Serializable
  public enum class CodeScanningDelegatedAlertDismissal(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  /**
   * Feature options for code scanning
   */
  @JvmInline
  @Serializable
  public value class CodeScanningOptions(
    @SerialName("allow_advanced")
    public val allowAdvanced: Boolean? = null,
  )

  @Serializable
  public enum class DependabotAlerts(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class DependabotDelegatedAlertDismissal(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class DependabotSecurityUpdates(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class DependencyGraph(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class DependencyGraphAutosubmitAction(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  /**
   * Feature options for Automatic dependency submission
   */
  @JvmInline
  @Serializable
  public value class DependencyGraphAutosubmitActionOptions(
    @SerialName("labeled_runners")
    public val labeledRunners: Boolean? = null,
  )

  @Serializable
  public enum class Enforcement(
    public val `value`: String,
  ) {
    @SerialName("enforced")
    Enforced("enforced"),
    @SerialName("unenforced")
    Unenforced("unenforced"),
    ;
  }

  @Serializable
  public enum class PrivateVulnerabilityReporting(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class SecretScanning(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class SecretScanningDelegatedAlertDismissal(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class SecretScanningDelegatedBypass(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  /**
   * Feature options for secret scanning delegated bypass
   */
  @JvmInline
  @Serializable
  public value class SecretScanningDelegatedBypassOptions(
    public val reviewers: List<Reviewers>? = null,
  ) {
    @Serializable
    public data class Reviewers(
      @SerialName("reviewer_id")
      public val reviewerId: Long,
      @SerialName("reviewer_type")
      public val reviewerType: ReviewerType,
      @SerialName("security_configuration_id")
      public val securityConfigurationId: Long? = null,
    ) {
      @Serializable
      public enum class ReviewerType {
        TEAM,
        ROLE,
      }
    }
  }

  @Serializable
  public enum class SecretScanningExtendedMetadata(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class SecretScanningGenericSecrets(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class SecretScanningNonProviderPatterns(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class SecretScanningPushProtection(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class SecretScanningValidityChecks(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }

  @Serializable
  public enum class TargetType(
    public val `value`: String,
  ) {
    @SerialName("global")
    Global("global"),
    @SerialName("organization")
    Organization("organization"),
    @SerialName("enterprise")
    Enterprise("enterprise"),
    ;
  }
}
