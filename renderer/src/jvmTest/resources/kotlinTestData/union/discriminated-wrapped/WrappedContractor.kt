package io.github.nomisrev.render.test.union.discriminated.wrapped

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class WrappedContractor(
  public val name: String,
)
