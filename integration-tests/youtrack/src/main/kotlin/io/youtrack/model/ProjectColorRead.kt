package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents color setting for one project on the board.
 */
@Serializable
public data class ProjectColorRead(
  public val id: String? = null,
  public val project: IssueFolderRead.Project? = null,
  public val color: FieldStyleRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
