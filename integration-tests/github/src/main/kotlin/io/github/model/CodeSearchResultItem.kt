package io.github.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Code Search Result Item
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileSize: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val language: Optional<String> = Optional.Absent,
  @SerialName("last_modified_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lastModifiedAt: Instant? = null,
  @SerialName("line_numbers")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lineNumbers: List<String>? = null,
  @SerialName("text_matches")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val textMatches: SearchResultTextMatches? = null,
)
