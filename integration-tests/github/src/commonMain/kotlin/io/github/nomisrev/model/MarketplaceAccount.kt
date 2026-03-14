package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MarketplaceAccount(
    val url: String,
    val id: Long,
    val type: String,
    @SerialName("node_id") val nodeId: String? = null,
    val login: String,
    val email: String? = null,
    @SerialName("organization_billing_email") val organizationBillingEmail: String? = null,
)
