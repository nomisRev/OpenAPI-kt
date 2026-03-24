package io.github.nomisrev.render.test.collection.freeform.default

import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@JvmInline
@Serializable
public value class Payload(
  @Required
  public val items: JsonArray = JsonArray(emptyList()),
)
