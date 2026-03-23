package io.github.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class BillingPremiumRequestUsageReportOrg(
  public val timePeriod: TimePeriod,
  public val organization: String,
  public val user: String? = null,
  public val product: String? = null,
  public val model: String? = null,
  public val usageItems: List<UsageItems>,
) {
  @Serializable
  public data class TimePeriod(
    public val year: Long,
    public val month: Long? = null,
    public val day: Long? = null,
  )

  @Serializable
  public data class UsageItems(
    public val product: String,
    public val sku: String,
    public val model: String,
    public val unitType: String,
    public val pricePerUnit: Double,
    public val grossQuantity: Double,
    public val grossAmount: Double,
    public val discountQuantity: Double,
    public val discountAmount: Double,
    public val netQuantity: Double,
    public val netAmount: Double,
  )
}
