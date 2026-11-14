package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents color setting for one project on the board.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectColorRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val project: IssueFolderRead.Project? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val color: FieldStyleRead? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
