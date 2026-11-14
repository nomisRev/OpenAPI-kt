package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SecurityAndAnalysis(
  @SerialName("advanced_security")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val advancedSecurity: AdvancedSecurity? = null,
  @SerialName("code_security")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val codeSecurity: CodeSecurity? = null,
  @SerialName("dependabot_security_updates")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
  @SerialName("secret_scanning")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanning: SecretScanning? = null,
  @SerialName("secret_scanning_push_protection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningPushProtection: SecretScanningPushProtection? = null,
  @SerialName("secret_scanning_non_provider_patterns")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
  @SerialName("secret_scanning_ai_detection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningAiDetection: SecretScanningAiDetection? = null,
  @SerialName("secret_scanning_delegated_alert_dismissal")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
  @SerialName("secret_scanning_delegated_bypass")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
  @SerialName("secret_scanning_delegated_bypass_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
) {
  /**
   * Enable or disable GitHub Advanced Security for the repository.
   *
   * For standalone Code Scanning or Secret Protection products, this parameter cannot be used.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AdvancedSecurity(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CodeSecurity(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class DependabotSecurityUpdates(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanning(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanningAiDetection(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanningDelegatedAlertDismissal(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanningDelegatedBypass(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanningDelegatedBypassOptions(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanningNonProviderPatterns(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SecretScanningPushProtection(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
