package io.github.nomisrev.render.test.object_.read.write.variants

import kotlin.Long
import kotlin.String
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class UserRead(
  public val id: Long,
  public val name: String,
  public val createdAt: Instant,
)
