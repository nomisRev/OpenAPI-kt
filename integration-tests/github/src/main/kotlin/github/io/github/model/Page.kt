package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for GitHub Pages for a repository.
 */
@Serializable
public data class Page(
  public val url: String,
  public val status: Status?,
  public val cname: String?,
  @SerialName("protected_domain_state")
  public val protectedDomainState: ProtectedDomainState? = null,
  @SerialName("pending_domain_unverified_at")
  public val pendingDomainUnverifiedAt: Instant? = null,
  @SerialName("custom_404")
  @Required
  public val custom404: Boolean = false,
  @SerialName("html_url")
  public val htmlUrl: String? = null,
  @SerialName("build_type")
  public val buildType: BuildType? = null,
  public val source: PagesSourceHash? = null,
  public val `public`: Boolean,
  @SerialName("https_certificate")
  public val httpsCertificate: PagesHttpsCertificate? = null,
  @SerialName("https_enforced")
  public val httpsEnforced: Boolean? = null,
) {
  @Serializable
  public enum class BuildType(
    public val `value`: String,
  ) {
    @SerialName("legacy")
    Legacy("legacy"),
    @SerialName("workflow")
    Workflow("workflow"),
    ;
  }

  @Serializable
  public enum class ProtectedDomainState(
    public val `value`: String,
  ) {
    @SerialName("pending")
    Pending("pending"),
    @SerialName("verified")
    Verified("verified"),
    @SerialName("unverified")
    Unverified("unverified"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("built")
    Built("built"),
    @SerialName("building")
    Building("building"),
    @SerialName("errored")
    Errored("errored"),
    ;
  }
}
