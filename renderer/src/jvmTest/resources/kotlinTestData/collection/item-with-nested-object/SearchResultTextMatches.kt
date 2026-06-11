package io.github.nomisrev.render.test.collection.item.with.nested.object_

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class SearchResultTextMatches(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    @SerialName("object_url")
    public val objectUrl: String? = null,
    @SerialName("object_type")
    public val objectType: String? = null,
    public val `property`: String? = null,
    public val fragment: String? = null,
    public val matches: List<Matches>? = null,
  ) {
    @Serializable
    public data class Matches(
      public val text: String? = null,
      public val indices: List<Long>? = null,
    )
  }
}
