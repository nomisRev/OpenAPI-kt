package io.github.nomisrev.render.test.client.bare.ref.alias

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class BareLabel(
  public val `value`: String,
)
