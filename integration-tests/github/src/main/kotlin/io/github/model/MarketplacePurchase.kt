package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Marketplace Purchase
 */
@Serializable
public data class MarketplacePurchase(
  public val url: String,
  public val type: String,
  public val id: Long,
  public val login: String,
  @SerialName("organization_billing_email")
  public val organizationBillingEmail: String? = null,
  public val email: String? = null,
  @SerialName("marketplace_pending_change")
  public val marketplacePendingChange: MarketplacePendingChange? = null,
  @SerialName("marketplace_purchase")
  public val marketplacePurchase: MarketplacePurchase,
) {
  @Serializable
  public data class MarketplacePendingChange(
    @SerialName("is_installed")
    public val isInstalled: Boolean? = null,
    @SerialName("effective_date")
    public val effectiveDate: String? = null,
    @SerialName("unit_count")
    public val unitCount: Long? = null,
    public val id: Long? = null,
    public val plan: MarketplaceListingPlan? = null,
  )

  @Serializable
  public data class MarketplacePurchase(
    @SerialName("billing_cycle")
    public val billingCycle: String? = null,
    @SerialName("next_billing_date")
    public val nextBillingDate: String? = null,
    @SerialName("is_installed")
    public val isInstalled: Boolean? = null,
    @SerialName("unit_count")
    public val unitCount: Long? = null,
    @SerialName("on_free_trial")
    public val onFreeTrial: Boolean? = null,
    @SerialName("free_trial_ends_on")
    public val freeTrialEndsOn: String? = null,
    @SerialName("updated_at")
    public val updatedAt: String? = null,
    public val plan: MarketplaceListingPlan? = null,
  )
}
