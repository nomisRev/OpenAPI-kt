package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Required

@Serializable
data class Page(
    val url: String,
    val status: Status?,
    val cname: String?,
    @SerialName("protected_domain_state") val protectedDomainState: ProtectedDomainState? = null,
    @SerialName("pending_domain_unverified_at") val pendingDomainUnverifiedAt: LocalDateTime? = null,
    @SerialName("custom_404") @Required val custom404: Boolean,
    @SerialName("html_url") val htmlUrl: String? = null,
    @SerialName("build_type") val buildType: BuildType? = null,
    val source: PagesSourceHash? = null,
    val public: Boolean,
    @SerialName("https_certificate") val httpsCertificate: PagesHttpsCertificate? = null,
    @SerialName("https_enforced") val httpsEnforced: Boolean? = null,
) {
    @Serializable
    enum class Status {
        @SerialName("built") Built, @SerialName("building") Building, @SerialName("errored") Errored;
    }

    @Serializable
    enum class ProtectedDomainState {
        @SerialName("pending") Pending, @SerialName("verified") Verified, @SerialName("unverified") Unverified;
    }

    @Serializable
    enum class BuildType {
        @SerialName("legacy") Legacy, @SerialName("workflow") Workflow;
    }
}
