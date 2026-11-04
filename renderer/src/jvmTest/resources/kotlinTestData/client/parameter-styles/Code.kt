package io.github.nomisrev.render.test.client.parameter.styles

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Code(
  public val `value`: String,
)
