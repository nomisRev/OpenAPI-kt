package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Api Overview
 */
@Serializable
public data class ApiOverview(
  @SerialName("verifiable_password_authentication")
  public val verifiablePasswordAuthentication: Boolean,
  @SerialName("ssh_key_fingerprints")
  public val sshKeyFingerprints: SshKeyFingerprints? = null,
  @SerialName("ssh_keys")
  public val sshKeys: List<String>? = null,
  public val hooks: List<String>? = null,
  @SerialName("github_enterprise_importer")
  public val githubEnterpriseImporter: List<String>? = null,
  public val web: List<String>? = null,
  public val api: List<String>? = null,
  public val git: List<String>? = null,
  public val packages: List<String>? = null,
  public val pages: List<String>? = null,
  public val importer: List<String>? = null,
  public val actions: List<String>? = null,
  @SerialName("actions_macos")
  public val actionsMacos: List<String>? = null,
  public val codespaces: List<String>? = null,
  public val dependabot: List<String>? = null,
  public val copilot: List<String>? = null,
  public val domains: Domains? = null,
) {
  @Serializable
  public data class Domains(
    public val website: List<String>? = null,
    public val codespaces: List<String>? = null,
    public val copilot: List<String>? = null,
    public val packages: List<String>? = null,
    public val actions: List<String>? = null,
    @SerialName("actions_inbound")
    public val actionsInbound: ActionsInbound? = null,
    @SerialName("artifact_attestations")
    public val artifactAttestations: ArtifactAttestations? = null,
  ) {
    @Serializable
    public data class ActionsInbound(
      @SerialName("full_domains")
      public val fullDomains: List<String>? = null,
      @SerialName("wildcard_domains")
      public val wildcardDomains: List<String>? = null,
    )

    @Serializable
    public data class ArtifactAttestations(
      @SerialName("trust_domain")
      public val trustDomain: String? = null,
      public val services: List<String>? = null,
    )
  }

  @Serializable
  public data class SshKeyFingerprints(
    @SerialName("SHA256_RSA")
    public val sha256Rsa: String? = null,
    @SerialName("SHA256_DSA")
    public val sha256Dsa: String? = null,
    @SerialName("SHA256_ECDSA")
    public val sha256Ecdsa: String? = null,
    @SerialName("SHA256_ED25519")
    public val sha256Ed25519: String? = null,
  )
}
