package io.github.nomisrev.render.test.client.alias.own.nullable

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class NullableAllOfNames(
  public val items: List<String>,
)
