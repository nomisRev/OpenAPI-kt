package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class PlainString(
  public val `value`: String,
)
