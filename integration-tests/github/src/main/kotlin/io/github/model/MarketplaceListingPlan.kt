package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Marketplace Listing Plan
 */
@Serializable
public data class MarketplaceListingPlan(
  public val url: String,
  @SerialName("accounts_url")
  public val accountsUrl: String,
  public val id: Long,
  public val number: Long,
  public val name: String,
  public val description: String,
  @SerialName("monthly_price_in_cents")
  public val monthlyPriceInCents: Long,
  @SerialName("yearly_price_in_cents")
  public val yearlyPriceInCents: Long,
  @SerialName("price_model")
  public val priceModel: PriceModel,
  @SerialName("has_free_trial")
  public val hasFreeTrial: Boolean,
  @SerialName("unit_name")
  public val unitName: String?,
  public val state: String,
  public val bullets: List<String>,
) {
  @Serializable
  public enum class PriceModel(
    public val `value`: String,
  ) {
    FREE("FREE"),
    @SerialName("FLAT_RATE")
    FLATRATE("FLAT_RATE"),
    @SerialName("PER_UNIT")
    PERUNIT("PER_UNIT"),
    ;
  }
}
