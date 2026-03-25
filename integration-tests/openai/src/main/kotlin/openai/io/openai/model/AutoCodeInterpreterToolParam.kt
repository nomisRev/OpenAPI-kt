package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Configuration for a code interpreter container. Optionally specify the IDs of the files to run the code on.
 */
@Serializable
public data class AutoCodeInterpreterToolParam(
  @Required
  public val type: Type = Type.Auto,
  @SerialName("file_ids")
  public val fileIds: List<String>? = null,
  @SerialName("memory_limit")
  public val memoryLimit: ContainerMemoryLimit? = null,
  @SerialName("network_policy")
  public val networkPolicy: NetworkPolicy? = null,
) {
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

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    ;
  }
}
