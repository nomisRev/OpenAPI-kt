package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ApiOverview(
    @SerialName("verifiable_password_authentication") val verifiablePasswordAuthentication: Boolean,
    @SerialName("ssh_key_fingerprints") val sshKeyFingerprints: SshKeyFingerprints? = null,
    @SerialName("ssh_keys") val sshKeys: List<String>? = null,
    val hooks: List<String>? = null,
    @SerialName("github_enterprise_importer") val githubEnterpriseImporter: List<String>? = null,
    val web: List<String>? = null,
    val api: List<String>? = null,
    val git: List<String>? = null,
    val packages: List<String>? = null,
    val pages: List<String>? = null,
    val importer: List<String>? = null,
    val actions: List<String>? = null,
    @SerialName("actions_macos") val actionsMacos: List<String>? = null,
    val codespaces: List<String>? = null,
    val dependabot: List<String>? = null,
    val copilot: List<String>? = null,
    val domains: Domains? = null,
) {
    @Serializable
    data class SshKeyFingerprints(
        @SerialName("SHA256_RSA") val sHA256RSA: String? = null,
        @SerialName("SHA256_DSA") val sHA256DSA: String? = null,
        @SerialName("SHA256_ECDSA") val sHA256ECDSA: String? = null,
        @SerialName("SHA256_ED25519") val sHA256ED25519: String? = null,
    )

    @Serializable
    data class Domains(
        val website: List<String>? = null,
        val codespaces: List<String>? = null,
        val copilot: List<String>? = null,
        val packages: List<String>? = null,
        val actions: List<String>? = null,
        @SerialName("actions_inbound") val actionsInbound: ActionsInbound? = null,
        @SerialName("artifact_attestations") val artifactAttestations: ArtifactAttestations? = null,
    ) {
        @Serializable
        data class ActionsInbound(
            @SerialName("full_domains") val fullDomains: List<String>? = null,
            @SerialName("wildcard_domains") val wildcardDomains: List<String>? = null,
        )

        @Serializable
        data class ArtifactAttestations(
            @SerialName("trust_domain") val trustDomain: String? = null,
            val services: List<String>? = null,
        )
    }
}
