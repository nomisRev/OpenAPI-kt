package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repository rule violation was detected
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RepositoryRuleViolationError(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: String? = null,
  @SerialName("documentation_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val documentationUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Metadata? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Metadata(
    @SerialName("secret_scanning")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val secretScanning: SecretScanning? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class SecretScanning(
      @SerialName("bypass_placeholders")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val bypassPlaceholders: List<BypassPlaceholders>? = null,
    ) {
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class BypassPlaceholders(
        @SerialName("placeholder_id")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val placeholderId: SecretScanningPushProtectionBypassPlaceholderId? = null,
        @SerialName("token_type")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val tokenType: String? = null,
      )
    }
  }
}
