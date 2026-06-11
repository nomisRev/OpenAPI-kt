package io.github.nomisrev.render.test.object_.read.write.variants

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class UserWrite(
  public val name: String,
)
