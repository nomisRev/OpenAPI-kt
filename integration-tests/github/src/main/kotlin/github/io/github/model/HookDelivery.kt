package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Delivery made by a webhook.
 */
@Serializable
public data class HookDelivery(
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
  public val url: String? = null,
  public val request: Request,
  public val response: Response,
) {
  @Serializable
  public data class Request(
    public val headers: JsonElement?,
    public val payload: JsonElement?,
  )

  @Serializable
  public data class Response(
    public val headers: JsonElement?,
    public val payload: String?,
  )
}
