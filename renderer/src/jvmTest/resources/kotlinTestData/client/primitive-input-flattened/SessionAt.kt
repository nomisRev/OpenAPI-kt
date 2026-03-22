package io.github.nomisrev.render.test.client.primitive.input.flattened

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class SessionAt(
  public val `value`: Instant,
)
