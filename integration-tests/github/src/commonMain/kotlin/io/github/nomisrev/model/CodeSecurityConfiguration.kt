package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class CodeSecurityConfiguration(
    val id: Long? = null,
    val name: String? = null,
    @SerialName("target_type") val targetType: TargetType? = null,
    val description: String? = null,
    @SerialName("advanced_security") val advancedSecurity: AdvancedSecurity? = null,
    @SerialName("dependency_graph") val dependencyGraph: DependencyGraph? = null,
    @SerialName("dependency_graph_autosubmit_action") val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
    @SerialName("dependency_graph_autosubmit_action_options") val dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
    @SerialName("dependabot_alerts") val dependabotAlerts: DependabotAlerts? = null,
    @SerialName("dependabot_security_updates") val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
    @SerialName("dependabot_delegated_alert_dismissal") val dependabotDelegatedAlertDismissal: DependabotDelegatedAlertDismissal? = null,
    @SerialName("code_scanning_options") val codeScanningOptions: CodeScanningOptions? = null,
    @SerialName("code_scanning_default_setup") val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
    @SerialName("code_scanning_default_setup_options") val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
    @SerialName("code_scanning_delegated_alert_dismissal") val codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
    @SerialName("secret_scanning") val secretScanning: SecretScanning? = null,
    @SerialName("secret_scanning_push_protection") val secretScanningPushProtection: SecretScanningPushProtection? = null,
    @SerialName("secret_scanning_delegated_bypass") val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
    @SerialName("secret_scanning_delegated_bypass_options") val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
    @SerialName("secret_scanning_validity_checks") val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
    @SerialName("secret_scanning_non_provider_patterns") val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
    @SerialName("secret_scanning_generic_secrets") val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
    @SerialName("secret_scanning_delegated_alert_dismissal") val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
    @SerialName("secret_scanning_extended_metadata") val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
    @SerialName("private_vulnerability_reporting") val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
    val enforcement: Enforcement? = null,
    val url: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
    @SerialName("created_at") val createdAt: LocalDateTime? = null,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
) {
    @Serializable
    enum class TargetType {
        @SerialName("global") Global, @SerialName("organization") Organization, @SerialName("enterprise") Enterprise;
    }

    @Serializable
    enum class AdvancedSecurity {
        @SerialName("enabled")
        Enabled,
        @SerialName("disabled")
        Disabled,
        @SerialName("code_security")
        CodeSecurity,
        @SerialName("secret_protection")
        SecretProtection;
    }

    @Serializable
    enum class DependencyGraph {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class DependencyGraphAutosubmitAction {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    @JvmInline
    value class DependencyGraphAutosubmitActionOptions(@SerialName("labeled_runners") val labeledRunners: Boolean? = null)

    @Serializable
    enum class DependabotAlerts {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class DependabotSecurityUpdates {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class DependabotDelegatedAlertDismissal {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    @JvmInline
    value class CodeScanningOptions(@SerialName("allow_advanced") val allowAdvanced: Boolean? = null)

    @Serializable
    enum class CodeScanningDefaultSetup {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    data class CodeScanningDefaultSetupOptions(
        @SerialName("runner_type") val runnerType: RunnerType? = null,
        @SerialName("runner_label") val runnerLabel: String? = null,
    ) {
        @Serializable
        enum class RunnerType {
            @SerialName("standard") Standard, @SerialName("labeled") Labeled, @SerialName("not_set") NotSet;
        }
    }

    @Serializable
    enum class CodeScanningDelegatedAlertDismissal {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class SecretScanning {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class SecretScanningPushProtection {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class SecretScanningDelegatedBypass {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    @JvmInline
    value class SecretScanningDelegatedBypassOptions(val reviewers: List<Reviewers>? = null) {
        @Serializable
        data class Reviewers(
            @SerialName("reviewer_id") val reviewerId: Long,
            @SerialName("reviewer_type") val reviewerType: ReviewerType,
            @SerialName("security_configuration_id") val securityConfigurationId: Long? = null,
        ) {
            @Serializable
            enum class ReviewerType {
                TEAM, ROLE;
            }
        }
    }

    @Serializable
    enum class SecretScanningValidityChecks {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class SecretScanningNonProviderPatterns {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class SecretScanningGenericSecrets {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class SecretScanningDelegatedAlertDismissal {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class SecretScanningExtendedMetadata {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class PrivateVulnerabilityReporting {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
    }

    @Serializable
    enum class Enforcement {
        @SerialName("enforced") Enforced, @SerialName("unenforced") Unenforced;
    }
}
