package io.github.nomisrev.render.test.client.allof.merged.alias

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class NonEmptyNames(
  public val items: List<String>,
)
