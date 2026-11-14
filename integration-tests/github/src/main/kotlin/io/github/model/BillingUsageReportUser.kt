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
public data class BillingUsageReportUser(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usageItems: List<UsageItems>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val repositoryName: String? = null,
  )
}
