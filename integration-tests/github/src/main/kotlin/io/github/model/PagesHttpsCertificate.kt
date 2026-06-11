package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PagesHttpsCertificate(
  public val state: State,
  public val description: String,
  public val domains: List<String>,
  @SerialName("expires_at")
  public val expiresAt: LocalDate? = null,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("new")
    New("new"),
    @SerialName("authorization_created")
    AuthorizationCreated("authorization_created"),
    @SerialName("authorization_pending")
    AuthorizationPending("authorization_pending"),
    @SerialName("authorized")
    Authorized("authorized"),
    @SerialName("authorization_revoked")
    AuthorizationRevoked("authorization_revoked"),
    @SerialName("issued")
    Issued("issued"),
    @SerialName("uploaded")
    Uploaded("uploaded"),
    @SerialName("approved")
    Approved("approved"),
    @SerialName("errored")
    Errored("errored"),
    @SerialName("bad_authz")
    BadAuthz("bad_authz"),
    @SerialName("destroy_pending")
    DestroyPending("destroy_pending"),
    @SerialName("dns_changed")
    DnsChanged("dns_changed"),
    ;
  }
}
