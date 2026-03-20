package io.github.nomisrev.render.test.object_.read.write.variants

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class UserWrite(
  public val name: String,
)
