package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Diff Entry
 */
@Serializable
public data class DiffEntry(
  public val sha: String?,
  public val filename: String,
  public val status: Status,
  public val additions: Long,
  public val deletions: Long,
  public val changes: Long,
  @SerialName("blob_url")
  public val blobUrl: String,
  @SerialName("raw_url")
  public val rawUrl: String,
  @SerialName("contents_url")
  public val contentsUrl: String,
  public val patch: String? = null,
  @SerialName("previous_filename")
  public val previousFilename: String? = null,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("added")
    Added("added"),
    @SerialName("removed")
    Removed("removed"),
    @SerialName("modified")
    Modified("modified"),
    @SerialName("renamed")
    Renamed("renamed"),
    @SerialName("copied")
    Copied("copied"),
    @SerialName("changed")
    Changed("changed"),
    @SerialName("unchanged")
    Unchanged("unchanged"),
    ;
  }
}
