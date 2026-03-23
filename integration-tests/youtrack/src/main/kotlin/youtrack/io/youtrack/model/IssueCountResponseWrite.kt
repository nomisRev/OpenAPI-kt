package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class IssueCountResponseWrite(
  public val count: Long? = null,
  public val unresolvedOnly: Boolean? = null,
  public val query: String? = null,
  public val folder: IssueFolderWrite? = null,
)
