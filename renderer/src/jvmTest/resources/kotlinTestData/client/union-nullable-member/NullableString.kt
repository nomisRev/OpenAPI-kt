package io.github.nomisrev.render.test.client.union.nullable.member

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class NullableString(
  public val `value`: String,
)
