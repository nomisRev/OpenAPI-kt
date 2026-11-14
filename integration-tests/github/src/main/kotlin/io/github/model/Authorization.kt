package io.github.model

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
 * The authorization for an OAuth app, GitHub App, or a Personal Access Token.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Authorization(
  public val id: Long,
  public val url: String,
  public val scopes: List<String>?,
  public val token: String,
  @SerialName("token_last_eight")
  public val tokenLastEight: String?,
  @SerialName("hashed_token")
  public val hashedToken: String?,
  public val app: App,
  public val note: String?,
  @SerialName("note_url")
  public val noteUrl: String?,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val fingerprint: String?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: Optional<NullableSimpleUser> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val installation: Optional<NullableScopedInstallation> = Optional.Absent,
  @SerialName("expires_at")
  public val expiresAt: Instant?,
) {
  @Serializable
  public data class App(
    @SerialName("client_id")
    public val clientId: String,
    public val name: String,
    public val url: String,
  )
}
