package io.github.nomisrev.render.test.object_.plain.model.no.read.suffix

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueKey(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val project: IssueFolderRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val numberInProject: Long? = null,
)
