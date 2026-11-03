package io.github.nomisrev.render.test.object_.nullable.alias.reference

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class OneOfAlias(
  public val `value`: String,
)
