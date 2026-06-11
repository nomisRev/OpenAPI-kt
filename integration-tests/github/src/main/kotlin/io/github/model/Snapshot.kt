package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Create a new snapshot of a repository's dependencies.
 */
@Serializable
public data class Snapshot(
  public val version: Long,
  public val job: Job,
  public val sha: String,
  public val ref: String,
  public val detector: Detector,
  public val metadata: Metadata? = null,
  public val manifests: List<Manifest>? = null,
  public val scanned: Instant,
) {
  /**
   * A description of the detector used.
   */
  @Serializable
  public data class Detector(
    public val name: String,
    public val version: String,
    public val url: String,
  )

  @Serializable
  public data class Job(
    public val id: String,
    public val correlator: String,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
  )
}
