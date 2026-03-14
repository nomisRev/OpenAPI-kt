package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RateLimitOverview(val resources: Resources, val rate: RateLimit) {
    @Serializable
    data class Resources(
        val core: RateLimit,
        val graphql: RateLimit? = null,
        val search: RateLimit,
        @SerialName("code_search") val codeSearch: RateLimit? = null,
        @SerialName("source_import") val sourceImport: RateLimit? = null,
        @SerialName("integration_manifest") val integrationManifest: RateLimit? = null,
        @SerialName("code_scanning_upload") val codeScanningUpload: RateLimit? = null,
        @SerialName("actions_runner_registration") val actionsRunnerRegistration: RateLimit? = null,
        val scim: RateLimit? = null,
        @SerialName("dependency_snapshots") val dependencySnapshots: RateLimit? = null,
        @SerialName("dependency_sbom") val dependencySbom: RateLimit? = null,
        @SerialName("code_scanning_autofix") val codeScanningAutofix: RateLimit? = null,
    )
}
