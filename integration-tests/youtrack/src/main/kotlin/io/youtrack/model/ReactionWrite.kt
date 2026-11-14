package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ReactionWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val author: UserWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reaction: String? = null,
)
