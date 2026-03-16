package io.github.nomisrev.render.test.union.discriminated.`enum`.case

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class EnumManual(
  public val kind: String,
)
