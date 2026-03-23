package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Delivery made by a webhook, without request and response information.
 */
@Serializable
public data class HookDeliveryItem(
  public val id: Long,
  public val guid: String,
  @SerialName("delivered_at")
  public val deliveredAt: Instant,
  public val redelivery: Boolean,
  public val duration: Double,
  public val status: String,
  @SerialName("status_code")
  public val statusCode: Long,
  public val event: String,
  public val action: String?,
  @SerialName("installation_id")
  public val installationId: Long?,
  @SerialName("repository_id")
  public val repositoryId: Long?,
  @SerialName("throttled_at")
  public val throttledAt: Instant? = null,
)
