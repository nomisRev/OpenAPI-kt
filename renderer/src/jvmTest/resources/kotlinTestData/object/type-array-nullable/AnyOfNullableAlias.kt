package io.github.nomisrev.render.test.object_.type.array.nullable

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class AnyOfNullableAlias(
  public val `value`: String,
)
