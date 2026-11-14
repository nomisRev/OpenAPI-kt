package io.github.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Resources(
    public val core: RateLimit,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val graphql: RateLimit? = null,
    public val search: RateLimit,
    @SerialName("code_search")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val codeSearch: RateLimit? = null,
    @SerialName("source_import")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sourceImport: RateLimit? = null,
    @SerialName("integration_manifest")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val integrationManifest: RateLimit? = null,
    @SerialName("code_scanning_upload")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val codeScanningUpload: RateLimit? = null,
    @SerialName("actions_runner_registration")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actionsRunnerRegistration: RateLimit? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val scim: RateLimit? = null,
    @SerialName("dependency_snapshots")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val dependencySnapshots: RateLimit? = null,
    @SerialName("dependency_sbom")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val dependencySbom: RateLimit? = null,
    @SerialName("code_scanning_autofix")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val codeScanningAutofix: RateLimit? = null,
  )
}
