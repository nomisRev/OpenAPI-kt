package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueCountResponseRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val count: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val unresolvedOnly: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val query: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val folder: IssueFolderRead? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
