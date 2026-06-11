package io.github.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Code Search Result Item
 */
@Serializable
public data class CodeSearchResultItem(
  public val name: String,
  public val path: String,
  public val sha: String,
  public val url: String,
  @SerialName("git_url")
  public val gitUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val repository: MinimalRepository,
  public val score: Double,
  @SerialName("file_size")
  public val fileSize: Long? = null,
  public val language: String? = null,
  @SerialName("last_modified_at")
  public val lastModifiedAt: Instant? = null,
  @SerialName("line_numbers")
  public val lineNumbers: List<String>? = null,
  @SerialName("text_matches")
  public val textMatches: SearchResultTextMatches? = null,
)
