package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateContainerBody(
  public val name: String,
  @SerialName("file_ids")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileIds: List<String>? = null,
  @SerialName("expires_after")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val expiresAfter: ExpiresAfter? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val skills: List<Skills>? = null,
  @SerialName("memory_limit")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val memoryLimit: MemoryLimit? = null,
  @SerialName("network_policy")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Allowlist(
      @SerialName("allowed_domains")
      public val allowedDomains: List<String>,
      @SerialName("domain_secrets")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val domainSecrets: List<ContainerNetworkPolicyDomainSecretParam>? = null,
    ) : NetworkPolicy
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Skills {
    @SerialName("skill_reference")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class SkillReference(
      @SerialName("skill_id")
      public val skillId: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
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
