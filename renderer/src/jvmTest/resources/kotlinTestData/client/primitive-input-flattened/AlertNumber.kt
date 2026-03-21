package io.github.nomisrev.render.test.client.primitive.input.flattened

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The security alert number.
 */
@JvmInline
@Serializable
public value class AlertNumber(
  public val `value`: Long,
)
