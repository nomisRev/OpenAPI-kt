package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class BillingUsageSummaryReportUser(
    val timePeriod: TimePeriod,
    val user: String,
    val repository: String? = null,
    val product: String? = null,
    val sku: String? = null,
    val usageItems: List<UsageItems>,
) {
    @Serializable
    data class TimePeriod(val year: Long, val month: Long? = null, val day: Long? = null)

    @Serializable
    data class UsageItems(
        val product: String,
        val sku: String,
        val unitType: String,
        val pricePerUnit: Double,
        val grossQuantity: Double,
        val grossAmount: Double,
        val discountQuantity: Double,
        val discountAmount: Double,
        val netQuantity: Double,
        val netAmount: Double,
    )
}
