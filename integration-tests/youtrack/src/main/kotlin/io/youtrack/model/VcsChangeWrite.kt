package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class VcsChangeWrite(
  public val version: String? = null,
  public val issue: IssueWrite? = null,
  public val state: Int? = null,
)
