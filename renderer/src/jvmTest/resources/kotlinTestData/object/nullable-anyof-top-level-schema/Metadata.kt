package io.github.nomisrev.render.test.object_.nullable.anyof.top.level.schema

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Set of key-value pairs attached to an object.
 */
@JvmInline
@Serializable
public value class Metadata(
  public val values: List<String>? = null,
)
