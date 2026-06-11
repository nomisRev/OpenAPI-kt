package io.github.nomisrev.render.test.client.primitive.input.flatten

import kotlin.Int
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Limit(
  @Required
  public val `value`: Int = 20,
)
