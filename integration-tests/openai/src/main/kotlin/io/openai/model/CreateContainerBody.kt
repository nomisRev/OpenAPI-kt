package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
public data class CreateContainerBody(
  public val name: String,
  @SerialName("file_ids")
  public val fileIds: List<String>? = null,
  @SerialName("expires_after")
  public val expiresAfter: ExpiresAfter? = null,
  public val skills: List<Skills>? = null,
  @SerialName("memory_limit")
  public val memoryLimit: MemoryLimit? = null,
  @SerialName("network_policy")
  public val networkPolicy: NetworkPolicy? = null,
) {
  /**
   * Container expiration time in seconds relative to the 'anchor' time.
   */
  @Serializable
  public data class ExpiresAfter(
    public val anchor: Anchor,
    public val minutes: Long,
  ) {
    @Serializable
    public enum class Anchor(
      public val `value`: String,
    ) {
      @SerialName("last_active_at")
      LastActiveAt("last_active_at"),
      ;
    }
  }

  @Serializable
  public enum class MemoryLimit {
    `1g`,
    `4g`,
    `16g`,
    `64g`,
  }

  /**
   * Network access policy for the container.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface NetworkPolicy {
    @Serializable
    @SerialName("disabled")
    public data object Disabled : NetworkPolicy

    @SerialName("allowlist")
    @Serializable
    public data class Allowlist(
      @SerialName("allowed_domains")
      public val allowedDomains: List<String>,
      @SerialName("domain_secrets")
      public val domainSecrets: List<ContainerNetworkPolicyDomainSecretParam>? = null,
    ) : NetworkPolicy
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Skills {
    @SerialName("skill_reference")
    @Serializable
    public data class SkillReference(
      @SerialName("skill_id")
      public val skillId: String,
      public val version: String? = null,
    ) : Skills

    @SerialName("inline")
    @Serializable
    public data class Inline(
      public val name: String,
      public val description: String,
      public val source: InlineSkillSourceParam,
    ) : Skills
  }
}
