package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * A unique encryption key
 */
@Serializable
public data class GpgKey(
  public val id: Long,
  public val name: String? = null,
  @SerialName("primary_key_id")
  public val primaryKeyId: Long?,
  @SerialName("key_id")
  public val keyId: String,
  @SerialName("public_key")
  public val publicKey: String,
  public val emails: List<Emails>,
  public val subkeys: List<Subkeys>,
  @SerialName("can_sign")
  public val canSign: Boolean,
  @SerialName("can_encrypt_comms")
  public val canEncryptComms: Boolean,
  @SerialName("can_encrypt_storage")
  public val canEncryptStorage: Boolean,
  @SerialName("can_certify")
  public val canCertify: Boolean,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("expires_at")
  public val expiresAt: Instant?,
  public val revoked: Boolean,
  @SerialName("raw_key")
  public val rawKey: String?,
) {
  @Serializable
  public data class Emails(
    public val email: String? = null,
    public val verified: Boolean? = null,
  )

  @Serializable
  public data class Subkeys(
    public val id: Long? = null,
    @SerialName("primary_key_id")
    public val primaryKeyId: Long? = null,
    @SerialName("key_id")
    public val keyId: String? = null,
    @SerialName("public_key")
    public val publicKey: String? = null,
    public val emails: List<Emails>? = null,
    public val subkeys: JsonArray? = null,
    @SerialName("can_sign")
    public val canSign: Boolean? = null,
    @SerialName("can_encrypt_comms")
    public val canEncryptComms: Boolean? = null,
    @SerialName("can_encrypt_storage")
    public val canEncryptStorage: Boolean? = null,
    @SerialName("can_certify")
    public val canCertify: Boolean? = null,
    @SerialName("created_at")
    public val createdAt: String? = null,
    @SerialName("expires_at")
    public val expiresAt: String? = null,
    @SerialName("raw_key")
    public val rawKey: String? = null,
    public val revoked: Boolean? = null,
  ) {
    @Serializable
    public data class Emails(
      public val email: String? = null,
      public val verified: Boolean? = null,
    )
  }
}
