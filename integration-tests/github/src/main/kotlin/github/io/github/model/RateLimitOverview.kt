package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Rate Limit Overview
 */
@Serializable
public data class RateLimitOverview(
  public val resources: Resources,
  public val rate: RateLimit,
) {
  @Serializable
  public data class Resources(
    public val core: RateLimit,
    public val graphql: RateLimit? = null,
    public val search: RateLimit,
    @SerialName("code_search")
    public val codeSearch: RateLimit? = null,
    @SerialName("source_import")
    public val sourceImport: RateLimit? = null,
    @SerialName("integration_manifest")
    public val integrationManifest: RateLimit? = null,
    @SerialName("code_scanning_upload")
    public val codeScanningUpload: RateLimit? = null,
    @SerialName("actions_runner_registration")
    public val actionsRunnerRegistration: RateLimit? = null,
    public val scim: RateLimit? = null,
    @SerialName("dependency_snapshots")
    public val dependencySnapshots: RateLimit? = null,
    @SerialName("dependency_sbom")
    public val dependencySbom: RateLimit? = null,
    @SerialName("code_scanning_autofix")
    public val codeScanningAutofix: RateLimit? = null,
  )
}
