package io.youtrack.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stores information about a project where the issue belongs or previously belonged. This entity appears as part of the ProjectActivityItem object.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueKey(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val project: IssueFolderRead.Project? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val numberInProject: Long? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
