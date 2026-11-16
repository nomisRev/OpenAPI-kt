package io.github.nomisrev.render.test.client.recursive.allof.ref

import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class NodeMap(
  public val values: Map<String, Node>,
)
