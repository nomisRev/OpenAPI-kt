package io.github.nomisrev.render.test.client.parameter.styles

import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Metadata(
  public val values: Map<String, String>,
)
