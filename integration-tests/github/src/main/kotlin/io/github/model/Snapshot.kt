package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Create a new snapshot of a repository's dependencies.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Snapshot(
  public val version: Long,
  public val job: Job,
  public val sha: String,
  public val ref: String,
  public val detector: Detector,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Metadata? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val manifests: Map<String, Manifest>? = null,
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Job(
    public val id: String,
    public val correlator: String,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
  )
}
