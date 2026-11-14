package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueLink(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val direction: Direction? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val linkType: IssueLinkTypeRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issues: List<IssueRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val trimmedIssues: List<IssueRead>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
) {
  @Serializable
  public enum class Direction {
    OUTWARD,
    INWARD,
    BOTH,
  }
}
