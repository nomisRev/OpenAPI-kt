package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MarketplaceAccount(
  public val url: String,
  public val id: Long,
  public val type: String,
  @SerialName("node_id")
  public val nodeId: String? = null,
  public val login: String,
  public val email: String? = null,
  @SerialName("organization_billing_email")
  public val organizationBillingEmail: String? = null,
)
