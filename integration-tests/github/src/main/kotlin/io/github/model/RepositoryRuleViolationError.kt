package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repository rule violation was detected
 */
@Serializable
public data class RepositoryRuleViolationError(
  public val message: String? = null,
  @SerialName("documentation_url")
  public val documentationUrl: String? = null,
  public val status: String? = null,
  public val metadata: Metadata? = null,
) {
  @JvmInline
  @Serializable
  public value class Metadata(
    @SerialName("secret_scanning")
    public val secretScanning: SecretScanning? = null,
  ) {
    @JvmInline
    @Serializable
    public value class SecretScanning(
      @SerialName("bypass_placeholders")
      public val bypassPlaceholders: List<BypassPlaceholders>? = null,
    ) {
      @Serializable
      public data class BypassPlaceholders(
        @SerialName("placeholder_id")
        public val placeholderId: SecretScanningPushProtectionBypassPlaceholderId? = null,
        @SerialName("token_type")
        public val tokenType: String? = null,
      )
    }
  }
}
