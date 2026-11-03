package io.github.nomisrev.render.test.client.single.ref.alias

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class AnyOfNames(
  public val items: List<String>,
)
