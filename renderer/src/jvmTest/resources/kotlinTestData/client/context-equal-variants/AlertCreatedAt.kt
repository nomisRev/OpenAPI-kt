package io.github.nomisrev.render.test.client.context.equal.variants

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class AlertCreatedAt(
  public val `value`: Instant,
)
