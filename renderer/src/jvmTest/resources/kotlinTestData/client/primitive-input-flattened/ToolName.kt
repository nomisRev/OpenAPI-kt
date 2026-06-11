package io.github.nomisrev.render.test.client.primitive.input.flattened

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class ToolName(
  public val `value`: String,
)
