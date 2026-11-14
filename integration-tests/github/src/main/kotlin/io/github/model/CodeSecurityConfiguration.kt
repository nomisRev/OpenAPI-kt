package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A code security configuration
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeSecurityConfiguration(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @SerialName("target_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val targetType: TargetType? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @SerialName("advanced_security")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val advancedSecurity: AdvancedSecurity? = null,
  @SerialName("dependency_graph")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependencyGraph: DependencyGraph? = null,
  @SerialName("dependency_graph_autosubmit_action")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
  @SerialName("dependency_graph_autosubmit_action_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
  @SerialName("dependabot_alerts")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabotAlerts: DependabotAlerts? = null,
  @SerialName("dependabot_security_updates")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
  @SerialName("dependabot_delegated_alert_dismissal")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabotDelegatedAlertDismissal:
      Optional<DependabotDelegatedAlertDismissal> = Optional.Absent,
  @SerialName("code_scanning_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val codeScanningOptions: Optional<CodeScanningOptions> = Optional.Absent,
  @SerialName("code_scanning_default_setup")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
  @SerialName("code_scanning_default_setup_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val codeScanningDefaultSetupOptions:
      Optional<CodeScanningDefaultSetupOptions> = Optional.Absent,
  @SerialName("code_scanning_delegated_alert_dismissal")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
  @SerialName("secret_scanning")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanning: SecretScanning? = null,
  @SerialName("secret_scanning_push_protection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningPushProtection: SecretScanningPushProtection? = null,
  @SerialName("secret_scanning_delegated_bypass")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
  @SerialName("secret_scanning_delegated_bypass_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
  @SerialName("secret_scanning_validity_checks")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
  @SerialName("secret_scanning_non_provider_patterns")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
  @SerialName("secret_scanning_generic_secrets")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
  @SerialName("secret_scanning_delegated_alert_dismissal")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
  @SerialName("secret_scanning_extended_metadata")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
  @SerialName("private_vulnerability_reporting")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enforcement: Enforcement? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: String? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CodeScanningDefaultSetupOptions(
    @SerialName("runner_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val runnerType: Optional<RunnerType> = Optional.Absent,
    @SerialName("runner_label")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val runnerLabel: Optional<String> = Optional.Absent,
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CodeScanningOptions(
    @SerialName("allow_advanced")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val allowAdvanced: Optional<Boolean> = Optional.Absent,
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class DependencyGraphAutosubmitActionOptions(
    @SerialName("labeled_runners")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanningDelegatedBypassOptions(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reviewers: List<Reviewers>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Reviewers(
      @SerialName("reviewer_id")
      public val reviewerId: Long,
      @SerialName("reviewer_type")
      public val reviewerType: ReviewerType,
      @SerialName("security_configuration_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
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
