package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class MarketplaceAccount(
  public val url: String,
  public val id: Long,
  public val type: String,
  @SerialName("node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nodeId: String? = null,
  public val login: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: Optional<String> = Optional.Absent,
  @SerialName("organization_billing_email")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationBillingEmail: Optional<String> = Optional.Absent,
)
