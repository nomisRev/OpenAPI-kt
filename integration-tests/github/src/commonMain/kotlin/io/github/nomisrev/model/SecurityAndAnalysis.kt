package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class SecurityAndAnalysis(
    @SerialName("advanced_security") val advancedSecurity: AdvancedSecurity? = null,
    @SerialName("code_security") val codeSecurity: CodeSecurity? = null,
    @SerialName("dependabot_security_updates") val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
    @SerialName("secret_scanning") val secretScanning: SecretScanning? = null,
    @SerialName("secret_scanning_push_protection") val secretScanningPushProtection: SecretScanningPushProtection? = null,
    @SerialName("secret_scanning_non_provider_patterns") val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
    @SerialName("secret_scanning_ai_detection") val secretScanningAiDetection: SecretScanningAiDetection? = null,
    @SerialName("secret_scanning_delegated_alert_dismissal") val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
    @SerialName("secret_scanning_delegated_bypass") val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
    @SerialName("secret_scanning_delegated_bypass_options") val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
) {
    @Serializable
    @JvmInline
    value class AdvancedSecurity(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class CodeSecurity(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class DependabotSecurityUpdates(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class SecretScanning(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class SecretScanningPushProtection(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class SecretScanningNonProviderPatterns(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class SecretScanningAiDetection(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class SecretScanningDelegatedAlertDismissal(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class SecretScanningDelegatedBypass(val status: Status? = null) {
        @Serializable
        enum class Status {
            @SerialName("enabled") Enabled, @SerialName("disabled") Disabled;
        }
    }

    @Serializable
    @JvmInline
    value class SecretScanningDelegatedBypassOptions(val reviewers: List<Reviewers>? = null) {
        @Serializable
        data class Reviewers(
            @SerialName("reviewer_id") val reviewerId: Long,
            @SerialName("reviewer_type") val reviewerType: ReviewerType,
        ) {
            @Serializable
            enum class ReviewerType {
                TEAM, ROLE;
            }
        }
    }
}
