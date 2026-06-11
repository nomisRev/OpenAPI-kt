package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerNetworkPolicyDomainSecretParam(
  public val domain: String,
  public val name: String,
  public val `value`: String,
)
