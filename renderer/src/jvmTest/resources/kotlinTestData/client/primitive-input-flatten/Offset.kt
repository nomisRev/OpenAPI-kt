package io.github.nomisrev.render.test.client.primitive.input.flatten

import kotlin.Int
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Offset(
  @Required
  public val `value`: Int = 0,
)
