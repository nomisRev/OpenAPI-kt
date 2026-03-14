package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MarketplaceListingPlan(
    val url: String,
    @SerialName("accounts_url") val accountsUrl: String,
    val id: Long,
    val number: Long,
    val name: String,
    val description: String,
    @SerialName("monthly_price_in_cents") val monthlyPriceInCents: Long,
    @SerialName("yearly_price_in_cents") val yearlyPriceInCents: Long,
    @SerialName("price_model") val priceModel: PriceModel,
    @SerialName("has_free_trial") val hasFreeTrial: Boolean,
    @SerialName("unit_name") val unitName: String?,
    val state: String,
    val bullets: List<String>,
) {
    @Serializable
    enum class PriceModel {
        FREE, @SerialName("FLAT_RATE") FLATRATE, @SerialName("PER_UNIT") PERUNIT;
    }
}
