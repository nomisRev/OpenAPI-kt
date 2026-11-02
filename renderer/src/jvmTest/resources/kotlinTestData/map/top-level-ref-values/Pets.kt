package io.github.nomisrev.render.test.map.top.level.ref.values

import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Pets(
  public val values: Map<String, Pet>,
)
