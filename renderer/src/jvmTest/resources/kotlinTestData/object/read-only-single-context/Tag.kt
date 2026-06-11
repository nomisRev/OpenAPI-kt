package io.github.nomisrev.render.test.object_.read.only.single.context

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Tag(
  public val id: Long,
  public val name: String,
)
