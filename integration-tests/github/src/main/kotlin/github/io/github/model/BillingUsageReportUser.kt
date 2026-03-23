package io.github.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class BillingUsageReportUser(
  public val usageItems: List<UsageItems>? = null,
) {
  @Serializable
  public data class UsageItems(
    public val date: String,
    public val product: String,
    public val sku: String,
    public val quantity: Long,
    public val unitType: String,
    public val pricePerUnit: Double,
    public val grossAmount: Double,
    public val discountAmount: Double,
    public val netAmount: Double,
    public val repositoryName: String? = null,
  )
}
