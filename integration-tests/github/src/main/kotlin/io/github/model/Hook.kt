package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Webhooks for repositories.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Hook(
  public val type: String,
  public val id: Long,
  public val name: String,
  public val active: Boolean,
  public val events: List<String>,
  public val config: WebhookConfig,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val url: String,
  @SerialName("test_url")
  public val testUrl: String,
  @SerialName("ping_url")
  public val pingUrl: String,
  @SerialName("deliveries_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val deliveriesUrl: String? = null,
  @SerialName("last_response")
  public val lastResponse: HookResponse,
)
