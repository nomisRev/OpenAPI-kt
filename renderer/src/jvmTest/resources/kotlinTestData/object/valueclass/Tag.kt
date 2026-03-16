package io.github.nomisrev.render.test.object_.valueclass

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Tag(
  public val `value`: String,
)
