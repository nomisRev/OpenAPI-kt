package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual Admin API key in an org.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AdminApiKey(
  public val `object`: String,
  public val id: String,
  public val name: String,
  @SerialName("redacted_value")
  public val redactedValue: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `value`: String? = null,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("last_used_at")
  public val lastUsedAt: Long?,
  public val owner: Owner,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Owner(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `object`: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @SerialName("created_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdAt: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val role: String? = null,
  )
}
