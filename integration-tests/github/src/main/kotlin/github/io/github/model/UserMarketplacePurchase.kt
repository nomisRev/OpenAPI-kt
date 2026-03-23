package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User Marketplace Purchase
 */
@Serializable
public data class UserMarketplacePurchase(
  @SerialName("billing_cycle")
  public val billingCycle: String,
  @SerialName("next_billing_date")
  public val nextBillingDate: Instant?,
  @SerialName("unit_count")
  public val unitCount: Long?,
  @SerialName("on_free_trial")
  public val onFreeTrial: Boolean,
  @SerialName("free_trial_ends_on")
  public val freeTrialEndsOn: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  public val account: MarketplaceAccount,
  public val plan: MarketplaceListingPlan,
)
