package io.github.nomisrev.render.test.collection.nullable

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class MaybeTags(
  public val items: List<String>?,
)
