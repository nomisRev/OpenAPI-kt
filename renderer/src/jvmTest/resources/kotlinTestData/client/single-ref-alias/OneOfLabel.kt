package io.github.nomisrev.render.test.client.single.ref.alias

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class OneOfLabel(
  public val `value`: String,
)
