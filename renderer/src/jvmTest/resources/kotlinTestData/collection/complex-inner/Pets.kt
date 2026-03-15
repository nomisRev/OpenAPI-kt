package io.github.nomisrev.render.test.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class Pets(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    public val id: String,
    public val name: String,
  )
}
