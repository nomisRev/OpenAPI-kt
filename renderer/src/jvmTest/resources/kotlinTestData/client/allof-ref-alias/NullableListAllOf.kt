package io.github.nomisrev.render.test.client.allof.ref.alias

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class NullableListAllOf(
  public val items: List<String>,
)
