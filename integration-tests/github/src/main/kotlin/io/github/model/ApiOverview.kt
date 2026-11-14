package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Api Overview
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ApiOverview(
  @SerialName("verifiable_password_authentication")
  public val verifiablePasswordAuthentication: Boolean,
  @SerialName("ssh_key_fingerprints")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sshKeyFingerprints: SshKeyFingerprints? = null,
  @SerialName("ssh_keys")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sshKeys: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hooks: List<String>? = null,
  @SerialName("github_enterprise_importer")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val githubEnterpriseImporter: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val web: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val api: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val git: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val packages: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pages: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val importer: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val actions: List<String>? = null,
  @SerialName("actions_macos")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val actionsMacos: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val codespaces: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabot: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val copilot: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val domains: Domains? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Domains(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val website: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val codespaces: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val copilot: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val packages: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actions: List<String>? = null,
    @SerialName("actions_inbound")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actionsInbound: ActionsInbound? = null,
    @SerialName("artifact_attestations")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val artifactAttestations: ArtifactAttestations? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ActionsInbound(
      @SerialName("full_domains")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fullDomains: List<String>? = null,
      @SerialName("wildcard_domains")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val wildcardDomains: List<String>? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ArtifactAttestations(
      @SerialName("trust_domain")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val trustDomain: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val services: List<String>? = null,
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SshKeyFingerprints(
    @SerialName("SHA256_RSA")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha256Rsa: String? = null,
    @SerialName("SHA256_DSA")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha256Dsa: String? = null,
    @SerialName("SHA256_ECDSA")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha256Ecdsa: String? = null,
    @SerialName("SHA256_ED25519")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha256Ed25519: String? = null,
  )
}
