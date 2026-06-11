package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueCountResponseRead(
  public val id: String? = null,
  public val count: Long? = null,
  public val unresolvedOnly: Boolean? = null,
  public val query: String? = null,
  public val folder: IssueFolderRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
