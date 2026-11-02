package io.github.nomisrev.render.test.map.`property`

import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Labels(
  public val values: Map<String, String>,
)
