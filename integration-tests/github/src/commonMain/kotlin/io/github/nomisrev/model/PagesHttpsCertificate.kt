package io.github.nomisrev.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PagesHttpsCertificate(
    val state: State,
    val description: String,
    val domains: List<String>,
    @SerialName("expires_at") val expiresAt: LocalDate? = null,
) {
    @Serializable
    enum class State {
        @SerialName("new")
        New,
        @SerialName("authorization_created")
        AuthorizationCreated,
        @SerialName("authorization_pending")
        AuthorizationPending,
        @SerialName("authorized")
        Authorized,
        @SerialName("authorization_revoked")
        AuthorizationRevoked,
        @SerialName("issued")
        Issued,
        @SerialName("uploaded")
        Uploaded,
        @SerialName("approved")
        Approved,
        @SerialName("errored")
        Errored,
        @SerialName("bad_authz")
        BadAuthz,
        @SerialName("destroy_pending")
        DestroyPending,
        @SerialName("dns_changed")
        DnsChanged;
    }
}
