package io.github.nomisrev.render.test.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Tag(
  public val `value`: String,
)
