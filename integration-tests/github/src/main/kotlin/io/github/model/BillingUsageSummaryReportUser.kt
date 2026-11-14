package io.github.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BillingUsageSummaryReportUser(
  public val timePeriod: TimePeriod,
  public val user: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repository: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val product: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sku: String? = null,
  public val usageItems: List<UsageItems>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TimePeriod(
    public val year: Long,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val month: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val day: Long? = null,
  )

  @Serializable
  public data class UsageItems(
    public val product: String,
    public val sku: String,
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
