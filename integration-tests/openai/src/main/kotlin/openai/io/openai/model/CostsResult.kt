package io.openai.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The aggregated costs details of the specific time bucket.
 */
@Serializable
public data class CostsResult(
  public val `object`: Object,
  public val amount: Amount? = null,
  @SerialName("line_item")
  public val lineItem: String? = null,
  @SerialName("project_id")
  public val projectId: String? = null,
) {
  /**
   * The monetary value in its associated currency.
   */
  @Serializable
  public data class Amount(
    public val `value`: Double? = null,
    public val currency: String? = null,
  )

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.costs.result")
    OrganizationCostsResult("organization.costs.result"),
    ;
  }
}
