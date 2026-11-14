package io.youtrack.model

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class VcsChangeWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val version: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issue: IssueWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val state: Int? = null,
)
