package io.github.nomisrev.render.test.collection.freeform

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
@JvmInline
public value class Payload(
  public val items: JsonArray,
)
