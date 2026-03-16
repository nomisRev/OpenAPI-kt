package io.github.nomisrev.render.test.union.discriminated.mixed

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class MixedUser(
  public val name: String,
)
