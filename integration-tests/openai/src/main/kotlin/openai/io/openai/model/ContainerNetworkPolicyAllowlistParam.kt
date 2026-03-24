package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerNetworkPolicyAllowlistParam(
  @Required
  public val type: Type = Type.Allowlist,
  @SerialName("allowed_domains")
  public val allowedDomains: List<String>,
  @SerialName("domain_secrets")
  public val domainSecrets: List<ContainerNetworkPolicyDomainSecretParam>? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("allowlist")
    Allowlist("allowlist"),
    ;
  }
}
