package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class BillingUsageReport(val usageItems: List<UsageItems>? = null) {
    @Serializable
    data class UsageItems(
        val date: String,
        val product: String,
        val sku: String,
        val quantity: Long,
        val unitType: String,
        val pricePerUnit: Double,
        val grossAmount: Double,
        val discountAmount: Double,
        val netAmount: Double,
        val organizationName: String,
        val repositoryName: String? = null,
    )
}
