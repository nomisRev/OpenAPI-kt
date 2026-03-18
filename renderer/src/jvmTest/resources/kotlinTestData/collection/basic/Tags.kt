package io.github.nomisrev.render.test.collection.basic

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Tags(
  public val items: List<String>,
)
