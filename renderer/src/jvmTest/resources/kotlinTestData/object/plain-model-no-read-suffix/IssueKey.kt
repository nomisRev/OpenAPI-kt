package io.github.nomisrev.render.test.object_.plain.model.no.read.suffix

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class IssueKey(
  public val id: String? = null,
  public val project: IssueFolderWrite? = null,
  public val numberInProject: Long? = null,
)
