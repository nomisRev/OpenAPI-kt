package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SecurityAndAnalysis(
  @SerialName("advanced_security")
  public val advancedSecurity: AdvancedSecurity? = null,
  @SerialName("code_security")
  public val codeSecurity: CodeSecurity? = null,
  @SerialName("dependabot_security_updates")
  public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
  @SerialName("secret_scanning")
  public val secretScanning: SecretScanning? = null,
  @SerialName("secret_scanning_push_protection")
  public val secretScanningPushProtection: SecretScanningPushProtection? = null,
  @SerialName("secret_scanning_non_provider_patterns")
  public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
  @SerialName("secret_scanning_ai_detection")
  public val secretScanningAiDetection: SecretScanningAiDetection? = null,
  @SerialName("secret_scanning_delegated_alert_dismissal")
  public val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
  @SerialName("secret_scanning_delegated_bypass")
  public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
  @SerialName("secret_scanning_delegated_bypass_options")
  public val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
) {
  /**
   * Enable or disable GitHub Advanced Security for the repository.
   *
   * For standalone Code Scanning or Secret Protection products, this parameter cannot be used.
   *
   */
  @JvmInline
  @Serializable
  public value class AdvancedSecurity(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

  @JvmInline
  @Serializable
  public value class CodeSecurity(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

  /**
   * Enable or disable Dependabot security updates for the repository.
   */
  @JvmInline
  @Serializable
  public value class DependabotSecurityUpdates(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

  @JvmInline
  @Serializable
  public value class SecretScanning(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

  @JvmInline
  @Serializable
  public value class SecretScanningAiDetection(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

  @JvmInline
  @Serializable
  public value class SecretScanningDelegatedAlertDismissal(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

  @JvmInline
  @Serializable
  public value class SecretScanningDelegatedBypass(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

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
    ) {
      @Serializable
      public enum class ReviewerType {
        TEAM,
        ROLE,
      }
    }
  }

  @JvmInline
  @Serializable
  public value class SecretScanningNonProviderPatterns(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }

  @JvmInline
  @Serializable
  public value class SecretScanningPushProtection(
    public val status: Status? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("enabled")
      Enabled("enabled"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }
}
