package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MarketplacePurchase(
    val url: String,
    val type: String,
    val id: Long,
    val login: String,
    @SerialName("organization_billing_email") val organizationBillingEmail: String? = null,
    val email: String? = null,
    @SerialName("marketplace_pending_change") val marketplacePendingChange: MarketplacePendingChange? = null,
    @SerialName("marketplace_purchase") val marketplacePurchase: MarketplacePurchase,
) {
    @Serializable
    data class MarketplacePendingChange(
        @SerialName("is_installed") val isInstalled: Boolean? = null,
        @SerialName("effective_date") val effectiveDate: String? = null,
        @SerialName("unit_count") val unitCount: Long? = null,
        val id: Long? = null,
        val plan: MarketplaceListingPlan? = null,
    )

    @Serializable
    data class MarketplacePurchase(
        @SerialName("billing_cycle") val billingCycle: String? = null,
        @SerialName("next_billing_date") val nextBillingDate: String? = null,
        @SerialName("is_installed") val isInstalled: Boolean? = null,
        @SerialName("unit_count") val unitCount: Long? = null,
        @SerialName("on_free_trial") val onFreeTrial: Boolean? = null,
        @SerialName("free_trial_ends_on") val freeTrialEndsOn: String? = null,
        @SerialName("updated_at") val updatedAt: String? = null,
        val plan: MarketplaceListingPlan? = null,
    )
}
