package io.github.nomisrev.render.test.client.recursive.allof.ref

import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class NodeList(
  public val items: List<Node>,
)
