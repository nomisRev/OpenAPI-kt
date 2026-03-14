package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserMarketplacePurchase(
    @SerialName("billing_cycle") val billingCycle: String,
    @SerialName("next_billing_date") val nextBillingDate: LocalDateTime?,
    @SerialName("unit_count") val unitCount: Long?,
    @SerialName("on_free_trial") val onFreeTrial: Boolean,
    @SerialName("free_trial_ends_on") val freeTrialEndsOn: LocalDateTime?,
    @SerialName("updated_at") val updatedAt: LocalDateTime?,
    val account: MarketplaceAccount,
    val plan: MarketplaceListingPlan,
)
